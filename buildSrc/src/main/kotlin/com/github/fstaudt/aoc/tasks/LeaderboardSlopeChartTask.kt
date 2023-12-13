package com.github.fstaudt.aoc.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.fstaudt.aoc.AdventOfCodePlugin.Companion.GROUP
import com.github.fstaudt.aoc.model.Leaderboard
import com.github.fstaudt.aoc.service.LeaderboardService
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG
import org.knowm.xchart.BitmapEncoder.saveBitmap
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler.LegendPosition.OutsideE
import java.awt.Color.WHITE
import java.awt.Font
import java.awt.Font.BOLD
import java.io.File
import javax.inject.Inject

abstract class LeaderboardSlopeChartTask : DefaultTask() {
    companion object {
        const val NAME = "leaderboardSlopeChart"
    }

    override fun getGroup() = GROUP
    override fun getDescription() = "Generate slope chart for top members of private leaderboard"

    @Input
    @Option(description = "year of advent calendar")
    lateinit var year: String

    @Input
    @Option(description = "ID of private leaderboard")
    lateinit var id: String

    @Input
    @Option(description = "number of top members displayed in slope chart")
    var top: String = "20"

    @Input
    @Option(description = "force download of leaderboard JSON - use wisely!")
    var force: Boolean = false

    @Input
    var sessionCookieFile: String = "cookie.txt"

    @get:Inject
    protected abstract val layout: ProjectLayout

    @Internal
    protected val jsonMapper = ObjectMapper().also {
        it.registerModule(KotlinModule.Builder().build())
    }

    @Internal
    protected val leaderboardService = LeaderboardService()

    @TaskAction
    fun generateSlopeChart() {
        File(layout.buildDirectory.asFile.get(), "aoc/leaderboards/$year").also { leaderboardsDir ->
            leaderboardsDir.mkdirs()
            File(leaderboardsDir, "$id.json").also { leaderboardFile ->
                if (force || !leaderboardFile.exists()) {
                    leaderboardFile.writeText(input())
                }
                generateSlopeChartFor(leaderboardFile)
            }
        }
    }

    private fun input(): String {
        logger.warn(
            """
                Please don't make frequent automated requests to Advent of code API.
                Avoid sending requests more often than once every 15 minutes (900 seconds).
                """.trimIndent()
        )
        val cookie = File(layout.projectDirectory.asFile, sessionCookieFile).readLines().first { it.isNotBlank() }
        return HttpClientBuilder.create().setDefaultHeaders(listOf(BasicHeader("Cookie", cookie))).build()
            .execute(HttpGet("https://adventofcode.com/$year/leaderboard/private/view/$id.json"))
            .entity.content.readAllBytes().let { String(it) }
    }

    private fun generateSlopeChartFor(leaderboardFile: File) {
        val leaderboard = jsonMapper.readValue(leaderboardFile, Leaderboard::class.java)
        val members = leaderboardService.topMembers(leaderboard, top.toInt())
        val numberOfDays = leaderboard.numberOfDays()

        val chart = XYChartBuilder()
            .width(numberOfDays * 150 + 200).height(members.size * 30)
            .xAxisTitle("Day")
            .title("Advent of Code 2023 - private leaderboard of ${leaderboard.owner()} ($id) - top $top")
            .build()
        chart.styler.apply {
            this.chartTitleFont = Font("SansSerif", BOLD, 24)
            yAxisMin = top.toDouble()
            yAxisMax = 1.0
            yAxisLeftWidthHint = 20
            legendPosition = OutsideE
            legendFont = Font("SansSerif", BOLD, 18)
            chartBackgroundColor = WHITE
            legendBorderColor = WHITE
            isPlotBorderVisible = false
            legendBackgroundColor = WHITE
            legendBorderColor = WHITE
            xAxisTickMarksColor = WHITE
            yAxisTickMarksColor = WHITE
        }
        members.forEach { chart.addSeries("${it.localScore} - ${it.name()}", (1..numberOfDays).toList(), it.rankings) }
        saveBitmap(chart, leaderboardFile.canonicalPath, PNG)
    }
}

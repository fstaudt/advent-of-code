package com.github.fstaudt.aoc.tasks

import com.github.fstaudt.aoc.AdventOfCodePlugin.Companion.GROUP
import com.github.fstaudt.aoc.model.Leaderboard
import com.github.fstaudt.aoc.service.JsonMapper
import com.github.fstaudt.aoc.service.LeaderboardService
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
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
import java.lang.String.format
import javax.inject.Inject

abstract class LeaderboardSlopeChartTask : DefaultTask() {
    companion object {
        const val NAME = "leaderboardSlopeChart"
    }

    override fun getGroup() = GROUP
    override fun getDescription() = "Generate slope chart for top members of private leaderboard"

    @get:Input
    @get:Option(option = "year", description = "year of advent calendar")
    @get:Optional
    abstract val year: Property<Int>

    @get:Input
    @get:Option(option = "id", description = "ID of private leaderboard")
    abstract val id: Property<String>

    @get:Input
    @get:Option(option = "top", description = "number of top members displayed in slope chart")
    @get:Optional
    abstract val top: Property<Int>

    @get:Input
    @get:Option(option = "from", description = "first day displayed in slope chart")
    @get:Optional
    abstract val from: Property<Int>

    @get:Input
    @get:Option(option = "until", description = "last day displayed in slope chart")
    @get:Optional
    abstract val until: Property<Int>

    @get:Input
    @get:Option(option = "final", description = "include final ranking in slope chart")
    @get:Optional
    abstract val final: Property<Boolean>

    @get:Input
    @get:Option(
        option = "min",
        description = "minimum required number of appearances in top for members not in top on last day"
    )
    @get:Optional
    abstract val min: Property<Int>

    @get:Input
    @get:Option(option = "force", description = "force download of leaderboard JSON - use wisely!")
    @get:Optional
    abstract val force: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val sessionCookieFile: Property<String>

    @get:Inject
    protected abstract val layout: ProjectLayout

    @get:Internal
    abstract val jsonMapper: Property<JsonMapper>

    @get:Internal
    abstract val leaderboardService: Property<LeaderboardService>

    @TaskAction
    fun generateSlopeChart() {
        val year = format("%04d", year.get())
        val id = id.get()
        File(layout.buildDirectory.asFile.get(), "aoc/leaderboards/$year").also { leaderboardsDir ->
            leaderboardsDir.mkdirs()
            File(leaderboardsDir, "$id.json").also { leaderboardFile ->
                if (force.get() || !leaderboardFile.exists()) {
                    leaderboardFile.writeText(input(year, id))
                }
                generateSlopeChartFor(leaderboardFile)
            }
        }
    }

    private fun input(year: String, id: String): String {
        logger.warn(
            """
                Please don't make frequent automated requests to Advent of code API.
                Avoid sending requests more often than once every 15 minutes (900 seconds).
                """.trimIndent()
        )
        val cookie = File(layout.projectDirectory.asFile, sessionCookieFile.get()).readLines().first { it.isNotBlank() }
        return HttpClientBuilder.create().setDefaultHeaders(listOf(BasicHeader("Cookie", cookie))).build()
            .execute(HttpGet("https://adventofcode.com/$year/leaderboard/private/view/$id.json"))
            .entity.content.readAllBytes().let { String(it) }
    }

    private fun generateSlopeChartFor(leaderboardFile: File) {
        val id = id.get()
        val top = top.get()
        val from = from.get()
        val until = until.get()
        val min = min.get()
        val final = final.get()
        val leaderboard = jsonMapper.get().readValue(leaderboardFile, Leaderboard::class.java)
        val members = leaderboardService.get().topMembers(leaderboard, top, until, min, final)
        val firstDay = runCatching { from.toInt() }.getOrElse { 1 }
        val lastDay = runCatching { until.toInt() }.getOrElse { 25 }.coerceAtMost(leaderboard.numberOfDays())
        val numberOfDays = lastDay - firstDay + 1
        val chart = XYChartBuilder()
            .width((numberOfDays + (if (final) 1 else 0)) * 120 + 150).height(members.size * 30)
            .xAxisTitle("Day")
            .title("Advent of Code 2023 - private leaderboard of ${leaderboard.owner()} ($id) - top $top")
            .build()
        chart.styler.apply {
            chartTitleFont = Font("SansSerif", BOLD, 24)
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
            setxAxisTickLabelsFormattingFunction { if (it > 25) "final" else "${it.toInt()}" }
            yAxisTickMarksColor = WHITE
        }
        val days = (firstDay..lastDay).toList() + (if (final) listOf(26) else emptyList())
        members.forEachIndexed { index, member ->
            val rankings = member.rankings.drop(firstDay - 1).take(numberOfDays).toMutableList().also {
                if (final) it += member.rankings.last()
            }
            val name =
                "${index + 1} - ${member.name()} (${if (final) member.localScore else member.localDailyScores[lastDay - 1]})"
            chart.addSeries(name, days, rankings)
        }
        saveBitmap(chart, leaderboardFile.canonicalPath, PNG)
    }
}

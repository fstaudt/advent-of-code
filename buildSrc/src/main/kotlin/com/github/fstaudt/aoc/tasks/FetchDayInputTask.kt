package com.github.fstaudt.aoc.tasks

import com.github.fstaudt.aoc.AdventOfCodePlugin.Companion.GROUP
import com.github.fstaudt.aoc.exception.AdventIsOverException
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.YEAR
import javax.inject.Inject

abstract class FetchDayInputTask : DefaultTask() {
    companion object {
        const val NAME = "fetchDayInput"
    }

    override fun getGroup() = GROUP
    override fun getDescription() = "Fetch input for day of advent calendar"

    @Input
    @Option(description = "day in advent calendar (defaults to current day of month)")
    var day: String = "${Calendar.getInstance().get(DAY_OF_MONTH)}"

    @Input
    @Option(description = "year of advent calendar (defaults to current year)")
    var year: String = "${Calendar.getInstance().get(YEAR)}"

    @Input
    var sessionCookieFile: String = "cookie.txt"

    @get:Inject
    protected abstract val layout: ProjectLayout

    @TaskAction
    fun fetchDayInput() {
        if (day > "25") throw AdventIsOverException(day)
        File(layout.projectDirectory.asFile, "src/main/resources").also { mainResources ->
            mainResources.mkdirs()
            File(mainResources, "day_$day.txt").writeText(input())
        }
    }

    private fun input(): String {
        val cookie = File(project.rootDir, sessionCookieFile).readLines().first { it.isNotBlank() }
        return HttpClientBuilder.create().setDefaultHeaders(listOf(BasicHeader("Cookie", cookie))).build()
            .execute(HttpGet("https://adventofcode.com/$year/day/$day/input"))
            .entity.content.readAllBytes().let { String(it) }
    }
}

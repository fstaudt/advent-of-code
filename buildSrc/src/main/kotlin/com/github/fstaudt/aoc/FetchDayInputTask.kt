package com.github.fstaudt.aoc

import com.github.fstaudt.aoc.AdventOfCodePlugin.Companion.GROUP
import com.github.fstaudt.aoc.service.input
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.ABSOLUTE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.YEAR
import javax.inject.Inject

abstract class FetchDayInputTask : DefaultTask() {
    override fun getGroup() = GROUP
    override fun getDescription() = "Fetch input for day of advent calendar"

    @Input
    @Option(description = "day in advent calendar (defaults to current day of month)")
    var day: String = "${Calendar.getInstance().get(DAY_OF_MONTH)}"

    @Input
    @Option(description = "year of advent calendar (defaults to current year)")
    var year: String = "${Calendar.getInstance().get(YEAR)}"

    @InputFile
    @PathSensitive(ABSOLUTE)
    var sessionCookieFile: File = File("cookie.txt")

    @get:Inject
    protected abstract val layout: ProjectLayout

    @TaskAction
    fun initDay() {
        File(layout.projectDirectory.asFile, "src/main/resources").also { mainResources ->
            mainResources.mkdirs()
            File(mainResources, "day_$day.txt").writeText(input(day, year, sessionCookieFile))
        }
    }
}

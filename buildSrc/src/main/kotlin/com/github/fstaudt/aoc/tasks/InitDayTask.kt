package com.github.fstaudt.aoc.tasks

import com.github.fstaudt.aoc.AdventOfCodePlugin.Companion.GROUP
import com.github.fstaudt.aoc.exception.AdventIsOverException
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.lang.String.format
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.YEAR
import javax.inject.Inject

abstract class InitDayTask : DefaultTask() {
    companion object {
        const val NAME = "initDay"
    }

    override fun getGroup() = GROUP
    override fun getDescription() = "Init sources for day of advent calendar"

    @get:Input
    @get:Option(option = "year", description = "year of advent calendar (defaults to current year)")
    @get:Optional
    abstract val year: Property<Int>

    @get:Input
    @get:Option(option = "day", description = "day in advent calendar (defaults to current day of month)")
    @get:Optional
    abstract val day: Property<Int>

    @get:Input
    @get:Option(option = "force", description = "overwrite existing sources")
    @get:Optional
    abstract val force: Property<Boolean>

    @get:Inject
    protected abstract val layout: ProjectLayout

    @TaskAction
    fun initDay() {
        val day = format("%02d", day.getOrElse(Calendar.getInstance().get(DAY_OF_MONTH)))
        val year = format("%4d", year.getOrElse(Calendar.getInstance().get(YEAR)))
        if (day.toInt() > 25) throw AdventIsOverException(day)
        val packageDir = "com/github/fstaudt/aoc$year/day$day"
        File(layout.projectDirectory.asFile, "src/main/kotlin/$packageDir").also { mainSources ->
            if (mainSources.exists() && !force.get()) throw Exception("Sources for day $day already exist.")
            mainSources.mkdirs()
            File(mainSources, "Day$day.kt").writeText(
                """
                package com.github.fstaudt.aoc$year.day$day

                import com.github.fstaudt.aoc.shared.Day
                import com.github.fstaudt.aoc.shared.Input.readInputLines

                fun main() {
                    Day$day().run()
                }

                class Day$day(fileName: String = "day_$day.txt") : Day {
                    override val input: List<String> = readInputLines(fileName)

                    override fun part1() = 0L

                    override fun part2() = 0L

                }
                """.trimIndent()
            )
        }
        File(layout.projectDirectory.asFile, "src/main/resources").also { mainResources ->
            mainResources.mkdirs()
            File(mainResources, "day_$day.txt").takeUnless { it.exists() }?.writeText("")
        }
        File(layout.projectDirectory.asFile, "src/test/kotlin/$packageDir").also { testSources ->
            testSources.mkdirs()
            File(testSources, "Day${day}Test.kt").writeText(
                """
                package com.github.fstaudt.aoc$year.day$day

                import org.assertj.core.api.Assertions.assertThat
                import org.junit.jupiter.api.Test
                
                class Day${day}Test {
                    @Test
                    fun `part 1 should produce expected result for example`() {
                        assertThat(Day$day("example_day$day.txt").part1()).isEqualTo(0)
                    }
                
                    @Test
                    fun `part 1 should produce expected result for my input`() {
                        assertThat(Day$day().part1()).isEqualTo(0)
                    }
                
                    @Test
                    fun `part 2 should produce expected result for example`() {
                        assertThat(Day$day("example_day$day.txt").part2()).isEqualTo(0)
                    }
                
                    @Test
                    fun `part 2 should produce expected result for my input`() {
                        assertThat(Day$day().part2()).isEqualTo(0)
                    }
                }
                """.trimIndent()
            )
        }
        File(layout.projectDirectory.asFile, "src/test/resources").also { testResources ->
            testResources.mkdirs()
            File(testResources, "example_day$day.txt").takeUnless { it.exists() }?.writeText("")
        }
    }
}

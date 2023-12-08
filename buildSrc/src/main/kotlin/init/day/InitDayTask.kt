package init.day

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
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

abstract class InitDayTask : DefaultTask() {
    override fun getGroup() = "generation"
    override fun getDescription() = "Init sources for day of advent calendar"

    @Input
    @Option(description = "day in advent calendar (defaults to current day of month)")
    var day: String = "${Calendar.getInstance().get(DAY_OF_MONTH)}"

    @Input
    @Option(description = "year of advent calendar (defaults to current year)")
    var year: String = "${Calendar.getInstance().get(YEAR)}"

    @InputFile
    @PathSensitive(ABSOLUTE)
    var sessionCookieFile: File = File("cookie.txt")

    @Input
    @Option(description = "use LongDay interface for long results")
    var long: Boolean = false

    @Input
    @Option(description = "overwrite existing sources")
    var force: Boolean = false

    @get:Inject
    protected abstract val layout: ProjectLayout

    @TaskAction
    fun initDay() {
        val packageDir = "com/github/fstaudt/aoc$year/day$day"
        File(layout.projectDirectory.asFile, "src/main/kotlin/$packageDir").also { mainSources ->
            if (mainSources.exists() && !force) throw Exception("Sources for day $day already exist.")
            mainSources.mkdirs()
            File(mainSources, "Day$day.kt").writeText(
                """
                package com.github.fstaudt.aoc$year.day$day

                import com.github.fstaudt.aoc$year.shared.${if (long) "Long" else ""}Day
                import com.github.fstaudt.aoc$year.shared.readInputLines

                fun main() {
                    Day$day().run()
                }

                class Day$day(fileName: String = "day_$day.txt") : ${if (long) "Long" else ""}Day {
                    override val input: List<String> = readInputLines(fileName)

                    override fun part1() = 0${if (long) "L" else ""}

                    override fun part2() = 0${if (long) "L" else ""}

                }
                """.trimIndent()
            )
        }
        File(layout.projectDirectory.asFile, "src/main/resources").also { mainResources ->
            mainResources.mkdirs()
            val cookie = sessionCookieFile.readLines().first { it.isNotBlank() }
            HttpClientBuilder.create().setDefaultHeaders(listOf(BasicHeader("Cookie", cookie))).build()
                .execute(HttpGet("https://adventofcode.com/$year/day/$day/input"))
                .entity.content.readAllBytes().let {
                    File(mainResources, "day_$day.txt").writeText(String(it))
                }
        }
        File(layout.projectDirectory.asFile, "src/test/kotlin/$packageDir").also { testSources ->
            testSources.mkdirs()
            File(testSources, "Day${day}Test.kt").writeText(
                """
                package com.github.fstaudt.aoc$year.day$day

                import org.assertj.core.api.Assertions.assertThat
                import org.junit.jupiter.api.Test
                
                class Day${day}Test {
                
                    companion object {
                        private const val EXAMPLE = "example_day$day.txt"
                    }
                
                    @Test
                    fun `part 1 should produce expected result for example`() {
                        assertThat(Day$day(EXAMPLE).part1()).isEqualTo(0)
                    }
                
                    @Test
                    fun `part 2 should produce expected result for example`() {
                        assertThat(Day$day(EXAMPLE).part2()).isEqualTo(0)
                    }
                
                    @Test
                    fun `part 1 should produce expected result for my input`() {
                        assertThat(Day$day().part1()).isEqualTo(0)
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
            File(testResources, "example_day$day.txt").writeText("")
        }
    }
}

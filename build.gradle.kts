import java.util.*
import java.util.Calendar.DAY_OF_MONTH

plugins {
    kotlin("jvm") version embeddedKotlinVersion
}

group = "com.github.fstaudt"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<InitDayTask>("initDay") {
    group = "generation"
    description = "Init sources for day of advent calendar"
}
abstract class InitDayTask : DefaultTask() {
    @Input
    @Option(description = "day in advent calendar (defaults to current day of month)")
    var day: String = "${Calendar.getInstance().get(DAY_OF_MONTH)}"

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
        val packageDir = "com/github/fstaudt/aoc2023/day$day"
        File(layout.projectDirectory.asFile, "src/main/kotlin/$packageDir").also {
            if (it.exists() && !force) throw Exception("Sources for day $day already exist.")
            it.mkdirs()
            File(it, "Day$day.kt").writeText(
                """
                package com.github.fstaudt.aoc2023.day$day

                import com.github.fstaudt.aoc2023.shared.${if (long) "Long" else ""}Day
                import com.github.fstaudt.aoc2023.shared.readInputLines

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
        File(layout.projectDirectory.asFile, "src/main/resources").also {
            it.mkdirs()
            File(it, "day_$day.txt").writeText("")
        }
        File(layout.projectDirectory.asFile, "src/test/kotlin/$packageDir").also {
            it.mkdirs()
            File(it, "Day${day}Test.kt").writeText(
                """
                package com.github.fstaudt.aoc2023.day$day

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
        File(layout.projectDirectory.asFile, "src/test/resources").also {
            it.mkdirs()
            File(it, "example_day$day.txt").writeText("")
        }

    }
}

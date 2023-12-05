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
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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
        val dir = File(layout.projectDirectory.asFile, "src/main/kotlin/com/github/fstaudt/aoc2023/day$day").also { it.mkdirs() }
        File(dir, "Day$day.kt").also {
            if (it.exists() && !force) throw Exception("Sources for day $day already exist.")
            it.writeText("""
                package com.github.fstaudt.aoc2023.day$day

                import com.github.fstaudt.aoc2023.shared.${if (long) "Long" else ""}Day
                import com.github.fstaudt.aoc2023.shared.readInputLines

                fun main() {
                    Day$day().run()
                }

                class Day$day : ${if (long) "Long" else ""}Day {
                    override val input: List<String> = readInputLines($day)

                    override fun part1() = 0${if (long) "L" else ""}

                    override fun part2() = 0${if (long) "L" else ""}

                }
            """.trimIndent())
        }
    }
}
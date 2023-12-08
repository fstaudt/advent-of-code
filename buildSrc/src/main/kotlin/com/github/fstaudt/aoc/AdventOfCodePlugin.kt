package com.github.fstaudt.aoc

import org.gradle.api.Plugin
import org.gradle.api.Project

class AdventOfCodePlugin : Plugin<Project> {
    companion object {
        const val GROUP = "advent of code"
    }
    override fun apply(project: Project) {
        with(project) {
            tasks.register("fetchDayInput", FetchDayInputTask::class.java)
            tasks.register("initDay", InitDayTask::class.java)
        }
    }
}

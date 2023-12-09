package com.github.fstaudt.aoc

import com.github.fstaudt.aoc.tasks.FetchDayInputTask
import com.github.fstaudt.aoc.tasks.InitDayTask
import com.github.fstaudt.aoc.tasks.LeaderboardSlopeChartTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class AdventOfCodePlugin : Plugin<Project> {
    companion object {
        const val GROUP = "advent of code"
    }

    override fun apply(project: Project) {
        with(project) {
            tasks.register(InitDayTask.NAME, InitDayTask::class.java)
            tasks.register(FetchDayInputTask.NAME, FetchDayInputTask::class.java)
            tasks.register(LeaderboardSlopeChartTask.NAME, LeaderboardSlopeChartTask::class.java)
        }
    }
}

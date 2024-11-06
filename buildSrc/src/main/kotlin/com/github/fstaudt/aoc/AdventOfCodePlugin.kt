package com.github.fstaudt.aoc

import com.github.fstaudt.aoc.service.JsonMapper
import com.github.fstaudt.aoc.service.LeaderboardService
import com.github.fstaudt.aoc.tasks.FetchDayInputTask
import com.github.fstaudt.aoc.tasks.InitDayTask
import com.github.fstaudt.aoc.tasks.LeaderboardSlopeChartTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.YEAR

class AdventOfCodePlugin : Plugin<Project> {
    companion object {
        const val GROUP = "advent of code"
        const val DEFAULT_SESSION_COOKIE_FILE = "cookie.txt"
    }

    override fun apply(project: Project) {
        with(project) {
            val leaderboardService =
                gradle.sharedServices.registerIfAbsent("leaderboard", LeaderboardService::class.java)
            val jsonMapper = gradle.sharedServices.registerIfAbsent("jsonMapper", JsonMapper::class.java)
            val extension = extensions.create("adventOfCode", AdventOfCodeExtension::class.java).apply {
                year.convention(Calendar.getInstance().get(YEAR))
            }
            if (project == rootProject) {
                tasks.register(LeaderboardSlopeChartTask.NAME, LeaderboardSlopeChartTask::class.java) { leaderboard ->
                    leaderboard.year.convention(extension.year)
                    leaderboard.top.convention(20)
                    leaderboard.from.convention(1)
                    leaderboard.until.convention(25)
                    leaderboard.final.convention(true)
                    leaderboard.min.convention(2)
                    leaderboard.force.convention(false)
                    leaderboard.sessionCookieFile.convention(DEFAULT_SESSION_COOKIE_FILE)
                    leaderboard.leaderboardService.set(leaderboardService)
                    leaderboard.jsonMapper.set(jsonMapper)
                }
            } else {
                tasks.register(InitDayTask.NAME, InitDayTask::class.java) { initDay ->
                    initDay.year.convention(extension.year)
                    initDay.day.convention(Calendar.getInstance().get(DAY_OF_MONTH))
                    initDay.force.convention(false)
                }
                tasks.register(FetchDayInputTask.NAME, FetchDayInputTask::class.java) { fetchDayInput ->
                    fetchDayInput.year.convention(extension.year)
                    fetchDayInput.day.convention(Calendar.getInstance().get(DAY_OF_MONTH))
                    fetchDayInput.sessionCookieFile.convention(DEFAULT_SESSION_COOKIE_FILE)
                }
            }
        }
    }
}

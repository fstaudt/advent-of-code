package com.github.fstaudt.aoc.service

import com.github.fstaudt.aoc.model.CompletionDaysLevelsFunctions.day
import com.github.fstaudt.aoc.model.CompletionPartsFunctions.part1
import com.github.fstaudt.aoc.model.CompletionPartsFunctions.part2
import com.github.fstaudt.aoc.model.Leaderboard
import com.github.fstaudt.aoc.model.Member
import kotlin.Int.Companion.MAX_VALUE

class LeaderboardService {

    fun topMembers(leaderboard: Leaderboard, top: Int): List<Member> {
        leaderboard.computeLocalDailyScores()
        for (day in 0..<leaderboard.numberOfDays()) {
            leaderboard.members.values.sortedWith(
                compareByDescending<Member> { it.localDailyScores[day] }
                    .thenBy { it.completionDayLevels.day(day)?.part2()?.starIndex ?: MAX_VALUE }
                    .thenBy { it.completionDayLevels.day(day)?.part1()?.starIndex ?: MAX_VALUE }
            ).let { members ->
                members.forEachIndexed { index, member ->
                    member.rankings.add((index + 1).takeIf { it <= top })
                }
            }
        }
        return leaderboard.members.values
            .sortedWith(compareByDescending<Member> { it.localScore }.thenBy { it.lastStarTimestamp })
            .filter { member -> member.rankings.any { it != null } }
    }

    private fun Leaderboard.computeLocalDailyScores() {
        members.values.forEach { it.localDailyScores.clear() }
        for (limitDay in 0..<numberOfDays()) {
            val start = java.time.LocalDate.of(event.toInt(), java.time.Month.DECEMBER, 1).let {
                java.time.OffsetDateTime.of(it, java.time.LocalTime.MIN, java.time.ZoneOffset.UTC).toEpochSecond()
            }
            val limit = (start + (limitDay + 1) * 86400)
            for (day in 0..limitDay) {
                sortedBy { it.completionDayLevels.day(day)?.part1()?.ifBefore(limit) }.let { members ->
                    members.forEachIndexed { index, member ->
                        if (member.completionDayLevels.day(day)?.part1()?.ifBefore(limit) != null) {
                            member.localDailyScore += members.size - index
                        }
                    }
                }
                sortedBy { it.completionDayLevels.day(day)?.part2()?.ifBefore(limit) }.let { members ->
                    members.forEachIndexed { index, member ->
                        if (member.completionDayLevels.day(day)?.part2()?.ifBefore(limit) != null) {
                            member.localDailyScore += members.size - index
                        }
                    }
                }
            }
            members.values.forEach {
                it.localDailyScores.add(it.localDailyScore)
                it.localDailyScore = 0
            }
        }
    }
}

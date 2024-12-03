package com.github.fstaudt.aoc.service

import com.github.fstaudt.aoc.model.CompletionDaysLevelsFunctions.day
import com.github.fstaudt.aoc.model.CompletionPartsFunctions.part1
import com.github.fstaudt.aoc.model.CompletionPartsFunctions.part2
import com.github.fstaudt.aoc.model.Leaderboard
import com.github.fstaudt.aoc.model.Member
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters.None
import kotlin.Int.Companion.MAX_VALUE

abstract class LeaderboardService : BuildService<None> {

    fun topMembers(
        leaderboard: Leaderboard,
        top: Int,
        until: Int = leaderboard.numberOfDays(),
        min: Int = 1,
        final: Boolean = false,
    ): List<Member> {
        leaderboard.computeLocalDailyScores(until)
        for (day in 0..<until) {
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
        if (final) {
            leaderboard.members.values.sortedWith(
                compareByDescending<Member> { it.localScore }.thenByDescending { it.lastStarTimestamp }
            ).let { members ->
                members.forEachIndexed { index, member ->
                    member.rankings.add((index + 1).takeIf { it <= top })
                }
            }
        }
        return leaderboard.members.values.sortedWith(
            if (final)
                compareByDescending<Member> { it.localScore }.thenByDescending { it.lastStarTimestamp }
            else
                compareByDescending<Member> { it.localDailyScores[until - 1] }.thenByDescending { it.lastStarTimestamp }
        ).filterIndexed { index, member ->
            (index < top || member.rankings.count { it != null } > min) && member.localScore > 0
        }
    }

    private fun Leaderboard.computeLocalDailyScores(until: Int) {
        members.values.forEach { it.localDailyScores.clear() }
        for (limitDay in 0..<until) {
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

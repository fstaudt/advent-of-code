package com.github.fstaudt.aoc.service

import com.github.fstaudt.aoc.model.CompletionDaysLevelsFunctions.day
import com.github.fstaudt.aoc.model.CompletionPartsFunctions.part1
import com.github.fstaudt.aoc.model.CompletionPartsFunctions.part2
import com.github.fstaudt.aoc.model.Leaderboard
import com.github.fstaudt.aoc.model.Member

class LeaderboardService {

    internal fun topMembers(leaderboard: Leaderboard, top: Int): List<Member> {
        for (day in 0..<leaderboard.numberOfDays()) {
            leaderboard.sortedBy { it.completionDayLevels.day(day)?.part1() }.let { members ->
                members.forEachIndexed { index, member ->
                    val previousDailyScore = if (day > 0) member.localDailyScores[day - 1] else 0
                    if (member.completionDayLevels.day(day)?.part1() != null) {
                        previousDailyScore + members.size - index
                    } else {
                        previousDailyScore
                    }.let { member.localDailyScores.add(it) }
                }
            }
            leaderboard.sortedBy { it.completionDayLevels.day(day)?.part2() }.let { members ->
                members.forEachIndexed { index, member ->
                    if (member.completionDayLevels.day(day)?.part2() != null) {
                        member.localDailyScores[day] += members.size - index
                    }
                }
            }
            leaderboard.members.values.sortedWith(
                compareByDescending<Member> { it.localDailyScores[day] }
                    .thenBy { it.completionDayLevels.day(day)?.part2()?.starIndex ?: Int.MAX_VALUE }
                    .thenBy { it.completionDayLevels.day(day)?.part1()?.starIndex ?: Int.MAX_VALUE }
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
}

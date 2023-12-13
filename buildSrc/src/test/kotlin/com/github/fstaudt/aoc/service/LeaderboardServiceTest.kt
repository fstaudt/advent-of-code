package com.github.fstaudt.aoc.service

import com.github.fstaudt.aoc.TestData.DAY1
import com.github.fstaudt.aoc.TestData.DAY2
import com.github.fstaudt.aoc.TestData.DAY3
import com.github.fstaudt.aoc.TestData.GHOST_ID
import com.github.fstaudt.aoc.TestData.OWNER_ID
import com.github.fstaudt.aoc.TestData.PLAYER_ID
import com.github.fstaudt.aoc.TestFunctions.leaderboard
import com.github.fstaudt.aoc.TestFunctions.member
import com.github.fstaudt.aoc.TestFunctions.part1
import com.github.fstaudt.aoc.TestFunctions.part2
import com.github.fstaudt.aoc.TestFunctions.withCompletionParts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeaderboardSlopeChartTaskTest {

    private val leaderboardService = LeaderboardService()
    private val owner = member(OWNER_ID, 12).withCompletionParts(
        1 to mapOf(part1(DAY1 + 1), part2(DAY1 + 2)),
        2 to mapOf(part1(DAY2 + 1), part2(DAY2 + 2)),
        3 to mapOf(part1(DAY3 + 1), part2(DAY3 + 2)),
    )
    private val player = member(PLAYER_ID, 6).withCompletionParts(
        1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
        2 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
        3 to mapOf(part1(DAY3 + 3), part2(DAY3 + 4)),
    )

    @Test
    fun `topMembers should return members ordered by local score`() {
        val topMembers = leaderboardService.topMembers(leaderboard(player, owner), 2)
        assertThat(topMembers).hasSize(2)
        assertThat(topMembers[0].id).isEqualTo(OWNER_ID)
        assertThat(topMembers[1].id).isEqualTo(PLAYER_ID)
    }

    @Test
    fun `topMembers should return only top members`() {
        val ghost = member("00000", 0)
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player, ghost), 2)
        assertThat(topMembers).hasSize(2)
        assertThat(topMembers[0].id).isEqualTo(OWNER_ID)
        assertThat(topMembers[1].id).isEqualTo(PLAYER_ID)
    }

    @Test
    fun `topMembers should return local daily scores`() {
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        with(topMembers[0]) {
            assertThat(localDailyScores).hasSize(3)
            assertThat(localDailyScores[0]).isEqualTo(4)
            assertThat(localDailyScores[1]).isEqualTo(8)
            assertThat(localDailyScores[2]).isEqualTo(12)
        }
        with(topMembers[1]) {
            assertThat(localDailyScores).hasSize(3)
            assertThat(localDailyScores[0]).isEqualTo(2)
            assertThat(localDailyScores[1]).isEqualTo(4)
            assertThat(localDailyScores[2]).isEqualTo(6)
        }
    }

    @Test
    fun `topMembers should only increment local daily score for completed parts`() {
        val player = member(PLAYER_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
            3 to mapOf(part1(DAY3 + 3)),
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        with(topMembers[0]) {
            assertThat(localDailyScores).hasSize(3)
            assertThat(localDailyScores[0]).isEqualTo(4)
            assertThat(localDailyScores[1]).isEqualTo(8)
            assertThat(localDailyScores[2]).isEqualTo(12)
        }
        with(topMembers[1]) {
            assertThat(localDailyScores).hasSize(3)
            assertThat(localDailyScores[0]).isEqualTo(2)
            assertThat(localDailyScores[1]).isEqualTo(2)
            assertThat(localDailyScores[2]).isEqualTo(3)
        }
    }

    @Test
    fun `topMembers should return local daily scores to 0 for ghost players`() {
        val ghost = member(GHOST_ID, 0)
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player, ghost), 3)
        with(topMembers[2]) {
            assertThat(localDailyScores).hasSize(3)
            assertThat(localDailyScores[0]).isEqualTo(0)
            assertThat(localDailyScores[1]).isEqualTo(0)
            assertThat(localDailyScores[2]).isEqualTo(0)
        }
    }

    @Test
    fun `topMembers should return rankings`() {
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        with(topMembers[0]) {
            assertThat(rankings).hasSize(3)
            assertThat(rankings[0]).isEqualTo(1)
            assertThat(rankings[1]).isEqualTo(1)
            assertThat(rankings[2]).isEqualTo(1)
        }
        with(topMembers[1]) {
            assertThat(rankings).hasSize(3)
            assertThat(rankings[0]).isEqualTo(2)
            assertThat(rankings[1]).isEqualTo(2)
            assertThat(rankings[2]).isEqualTo(2)
        }
    }

    @Test
    fun `topMembers should return null ranking when member is not in top`() {
        val owner = member(OWNER_ID, 10).withCompletionParts(
            1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
            2 to mapOf(part1(DAY2 + 1), part2(DAY2 + 2)),
            3 to mapOf(part1(DAY3 + 1), part2(DAY3 + 2)),
        )
        val player = member(PLAYER_ID, 8).withCompletionParts(
            1 to mapOf(part1(DAY1 + 1), part2(DAY1 + 2)),
            2 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
            3 to mapOf(part1(DAY3 + 3), part2(DAY3 + 4)),
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 1)
        assertThat(topMembers).hasSize(2)
        with(topMembers[0]) {
            assertThat(rankings).hasSize(3)
            assertThat(rankings[0]).isNull()
            assertThat(rankings[1]).isEqualTo(1)
            assertThat(rankings[2]).isEqualTo(1)
        }
        with(topMembers[1]) {
            assertThat(rankings).hasSize(3)
            assertThat(rankings[0]).isEqualTo(1)
            assertThat(rankings[1]).isNull()
            assertThat(rankings[2]).isNull()
        }
    }
}

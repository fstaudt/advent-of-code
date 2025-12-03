package com.github.fstaudt.aoc.service

import com.github.fstaudt.aoc.TestData.DAY1
import com.github.fstaudt.aoc.TestData.DAY2
import com.github.fstaudt.aoc.TestData.DAY3
import com.github.fstaudt.aoc.TestData.DAY4
import com.github.fstaudt.aoc.TestData.DAY5
import com.github.fstaudt.aoc.TestData.GHOST_ID
import com.github.fstaudt.aoc.TestData.OWNER_ID
import com.github.fstaudt.aoc.TestData.PLAYER_ID
import com.github.fstaudt.aoc.TestFunctions.leaderboard
import com.github.fstaudt.aoc.TestFunctions.member
import com.github.fstaudt.aoc.TestFunctions.part1
import com.github.fstaudt.aoc.TestFunctions.part2
import com.github.fstaudt.aoc.TestFunctions.withCompletionParts
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.services.BuildServiceParameters.None
import org.junit.jupiter.api.Test

class LeaderboardServiceTest {

    class TestLeaderboardService : LeaderboardService() {
        override fun getParameters(): None {
            throw UnsupportedOperationException("Not supported yet.")
        }
    }

    private val leaderboardService = TestLeaderboardService()
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
  fun `topMembers should return members ordered by local score then by last star timestamp`() {
    val firstByLastStar = member(OWNER_ID, 8, DAY2 + 2).withCompletionParts(
      1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
      2 to mapOf(part1(DAY2 + 1), part2(DAY2 + 2)),
    )
    val secondByLastStar = member(PLAYER_ID, 8, DAY2 + 4).withCompletionParts(
      1 to mapOf(part1(DAY1 + 1), part2(DAY1 + 2)),
      2 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
    )
    val topMembers = leaderboardService.topMembers(leaderboard(firstByLastStar, secondByLastStar), 2)
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
    fun `topMembers should not return members that did not appear enough times in top`() {
        val owner = member(OWNER_ID, 10).withCompletionParts(
            1 to mapOf(part1(DAY1 + 5), part2(DAY1 + 6)),
            2 to mapOf(part1(DAY2 + 1), part2(DAY2 + 2)),
            3 to mapOf(part1(DAY3 + 3), part2(DAY3 + 4)),
            4 to mapOf(part1(DAY4 + 1), part2(DAY4 + 2)),
            5 to mapOf(part1(DAY5 + 1), part2(DAY5 + 2)),
        )
        val player = member(PLAYER_ID, 8).withCompletionParts(
            1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
            2 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
            3 to mapOf(part1(DAY3 + 1), part2(DAY3 + 2)),
            4 to mapOf(part1(DAY4 + 3), part2(DAY4 + 4)),
            5 to mapOf(part1(DAY5 + 3), part2(DAY5 + 4)),
        )
        val topOnFirstDay = member(GHOST_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY1 + 1), part2(DAY1 + 2))
        )

        val topMembers = leaderboardService.topMembers(leaderboard(owner, player, topOnFirstDay), 2, min = 2)
        assertThat(topMembers).hasSize(2)
        assertThat(topMembers[0].rankings).isEqualTo(listOf(null, 1, 2, 1, 1))
        assertThat(topMembers[1].rankings).isEqualTo(listOf(2, 2, 1, 2, 2))
    }

    @Test
    fun `topMembers should return local daily scores`() {
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        assertThat(topMembers[0].localDailyScores).isEqualTo(listOf(4, 8, 12))
        assertThat(topMembers[1].localDailyScores).isEqualTo(listOf(2, 4, 6))
    }

    @Test
    fun `topMembers should only increment local daily score for completed parts`() {
        val player = member(PLAYER_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
            3 to mapOf(part1(DAY3 + 3)),
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        assertThat(topMembers[0].localDailyScores).isEqualTo(listOf(4, 8, 12))
        assertThat(topMembers[1].localDailyScores).isEqualTo(listOf(2, 2, 3))
    }

    @Test
    fun `topMembers should exclude ghost members by default`() {
        val ghost = member(GHOST_ID, 0)
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player, ghost), 3)
        assertThat(topMembers).hasSize(2)
    }

    @Test
    fun `topMembers should include ghost members when enabled`() {
        val ghost = member(GHOST_ID, 0)
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player, ghost), 3, ghosts = true)
        assertThat(topMembers).hasSize(3)
    }

    @Test
    fun `topMembers should only increment local daily score for parts completed before EOD`() {
        val player = member(PLAYER_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY1 + 3), part2(DAY1 + 4)),
            3 to mapOf(part1(DAY3 + 3), part2(DAY3 + 4)),
            2 to mapOf(part1(DAY3 + 5), part2(DAY3 + 6)),
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        assertThat(topMembers[0].localDailyScores).isEqualTo(listOf(4, 8, 12))
        assertThat(topMembers[1].localDailyScores).isEqualTo(listOf(2, 2, 6))
    }

    @Test
    fun `topMembers should return rankings`() {
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        assertThat(topMembers[0].rankings).isEqualTo(listOf(1, 1, 1))
        assertThat(topMembers[1].rankings).isEqualTo(listOf(2, 2, 2))
    }

    @Test
    fun `topMembers should keep ranking of previous day when members haven't completed current day`() {
        val owner = member(OWNER_ID, 4).withCompletionParts(
            3 to mapOf(part1(DAY3 + 4), part2(DAY3 + 5))
        )
        val player = member(PLAYER_ID, 5).withCompletionParts(
            1 to mapOf(part1(DAY3 + 1), part2(DAY3 + 2)),
            3 to mapOf(part1(DAY3 + 3))
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2, until = 3)
        assertThat(topMembers[0].id).isEqualTo(PLAYER_ID)
        assertThat(topMembers[0].rankings).isEqualTo(listOf(1, 1, 1))
        assertThat(topMembers[1].id).isEqualTo(OWNER_ID)
        assertThat(topMembers[1].rankings).isEqualTo(listOf(2, 2, 2))
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
        assertThat(topMembers).hasSize(1)
        assertThat(topMembers[0].rankings).isEqualTo(listOf(null, 1, 1))
    }

    @Test
    fun `topMembers should return rankings based on local daily score for parts completed before EOD`() {
        val owner = member(OWNER_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY1 + 1), part2(DAY1 + 2)),
            2 to mapOf(part1(DAY3 + 1), part2(DAY3 + 2)),
            3 to mapOf(part1(DAY3 + 3), part2(DAY3 + 4)),
        )
        val player = member(PLAYER_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
            2 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
            3 to mapOf(part1(DAY3 + 5), part2(DAY3 + 6)),
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2)
        assertThat(topMembers[0].rankings).isEqualTo(listOf(1, 2, 1))
        assertThat(topMembers[1].rankings).isEqualTo(listOf(2, 1, 2))
    }

    @Test
    fun `topMembers should return extra ranking based on current local score when final is true`() {
        val owner = member(OWNER_ID, 5).withCompletionParts(
            1 to mapOf(part1(DAY1 + 1), part2(DAY1 + 2)),
            2 to mapOf(part1(DAY3 + 1), part2(DAY3 + 2)),
            3 to mapOf(part1(DAY3 + 3), part2(DAY3 + 4)),
        )
        val player = member(PLAYER_ID, 3).withCompletionParts(
            1 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
            2 to mapOf(part1(DAY2 + 3), part2(DAY2 + 4)),
            3 to mapOf(part1(DAY3 + 5), part2(DAY3 + 6)),
        )
        val topMembers = leaderboardService.topMembers(leaderboard(owner, player), 2, 2, final = true)
        assertThat(topMembers[0].rankings).isEqualTo(listOf(1, 2, 1))
        assertThat(topMembers[1].rankings).isEqualTo(listOf(2, 1, 2))
    }
}

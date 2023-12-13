package com.github.fstaudt.aoc

import com.github.fstaudt.aoc.TestData.DAY3
import com.github.fstaudt.aoc.TestData.EVENT
import com.github.fstaudt.aoc.TestData.OWNER_ID
import com.github.fstaudt.aoc.model.CompletionPart
import com.github.fstaudt.aoc.model.CompletionParts
import com.github.fstaudt.aoc.model.Leaderboard
import com.github.fstaudt.aoc.model.Member

internal object TestFunctions {
    fun part1(timestamp: Int) = 1 to CompletionPart(timestamp - 1L, timestamp - 1)
    fun part2(timestamp: Int) = 2 to CompletionPart(timestamp - 1L, timestamp - 1)
    fun member(id: String, localScore: Int) = id to Member(id, id, 0, DAY3, localScore, 0, emptyMap())
    fun leaderboard(vararg members: Pair<String, Member>) =
        Leaderboard(EVENT, OWNER_ID, mapOf(*members))

    fun Pair<String, Member>.withCompletionParts(vararg completionParts: Pair<Int, CompletionParts>) = second.run {
        first to Member(id, id, stars, lastStarTimestamp, localScore, globalScore, mapOf(*completionParts))
    }
}

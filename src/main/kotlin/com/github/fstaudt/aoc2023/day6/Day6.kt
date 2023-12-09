package com.github.fstaudt.aoc2023.day6

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import com.github.fstaudt.aoc2023.shared.splitLongs
import com.github.fstaudt.aoc2023.shared.toGroupValue

fun main() {
    Day6().run()
}

class Day6(fileName: String = "day_6.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val times = input[0].toGroupValue("Time: +(.*)", 1).splitLongs()
    private val distances = input[1].toGroupValue("Distance: +(.*)", 1).splitLongs()

    override fun part1() = productOfWinningWays()
    override fun part2() = longWinningWays()

    private fun productOfWinningWays(): Long {
        val races = times.mapIndexed { i, time -> Race(time, distances[i]) }
        return races.map { it.winningWays() }.reduce { product, number -> product * number }
    }

    private fun longWinningWays(): Long {
        val longTime = times.fold("") { long, time -> "$long$time" }.toLong()
        val longDistance = distances.fold("") { long, distance -> "$long$distance" }.toLong()
        return Race(longTime, longDistance).winningWays()
    }

    data class Race(val time: Long, val distance: Long) {
        fun winningWays() = (1..<time).count { it * (time - it) > distance }.toLong()
    }
}

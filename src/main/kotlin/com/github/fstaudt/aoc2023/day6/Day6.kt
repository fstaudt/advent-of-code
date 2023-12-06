package com.github.fstaudt.aoc2023.day6

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines

fun main() {
    Day6().run()
}

class Day6 : Day {
    override val input: List<String> = readInputLines(6)
    private val times = Regex("Time: +(.*)").find(input[0])!!.groupValues[1]
        .split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    private val distances = Regex("Distance: +(.*)").find(input[1])!!.groupValues[1]
        .split(" ").filter { it.isNotBlank() }.map { it.toLong() }

    override fun part1(): Int {
        val races = times.mapIndexed { i, time -> Race(time, distances[i]) }
        return races.map { it.winningWays() }.reduce { product, number -> product * number }
    }

    override fun part2(): Int {
        val longTime = times.fold("") { long, time -> "$long$time" }.toLong()
        val longDistance = distances.fold("") { long, distance -> "$long$distance" }.toLong()
        return Race(longTime, longDistance).winningWays()
    }

    data class Race(val time: Long, val distance: Long) {
        fun winningWays() = (1..<time).count { it * (time - it) > distance }
    }
}

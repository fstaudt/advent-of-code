package com.github.fstaudt.aoc2023.day9

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import com.github.fstaudt.aoc2023.shared.splitLongs

fun main() {
    Day9().run()
}

class Day9(fileName: String = "day_9.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val values = input.map { it.splitLongs() }

    override fun part1() = sumNextValues()
    override fun part2() = sumPreviousValues()

    private fun sumNextValues(): Long = values.sumOf { it.toNextValue() }
    private fun sumPreviousValues(): Long = values.sumOf { it.toPreviousValue() }
    private fun List<Long>.toNextValue(): Long = if (all { it == 0L }) 0 else last() + diffs().toNextValue()
    private fun List<Long>.toPreviousValue(): Long = if (all { it == 0L }) 0 else first() - diffs().toPreviousValue()
    private fun List<Long>.diffs() = windowed(2).map { it[1] - it[0] }
}

package com.github.fstaudt.aoc2023.day12

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import com.github.fstaudt.aoc2023.shared.splitInts
import com.github.fstaudt.aoc2023.shared.splitNotEmpty

fun main() {
    Day12().run()
}

class Day12(fileName: String = "day_12.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val field = input.map { it.toRow() }

    data class Row(val springs: String, val numbers: List<Int>)

    private fun String.toRow() = Row(split(" ")[0], split(" ")[1].splitInts(','))
    override fun part1() = sumCountOfArrangements()

    override fun part2() = sumCountOfArrangementsWhenUnfolded()

    private fun sumCountOfArrangements(): Long {
        return field.sumOf { it.toBruteForceArrangements() }
    }

    private fun Row.toBruteForceArrangements(): Long {
        if (springs.none { it == '?' }) {
            return if (springs.splitNotEmpty('.').map { it.length } == numbers) 1 else 0
        }
        return Row(springs.replaceFirst('?', '.'), numbers).toBruteForceArrangements() +
                Row(springs.replaceFirst('?', '#'), numbers).toBruteForceArrangements()
    }

    private fun sumCountOfArrangementsWhenUnfolded(): Long {
        return field
            .map { row -> Row(row.springs.let { "$it?".repeat(4) + it }, (1..5).flatMap { row.numbers }) }
            .map { it.toArrangements() }
            .sumOf { it }
    }

    private fun Row.toArrangements(): Long {
        // TODO
        return numbers.toRegex().pattern.length.toLong()
    }

    private fun List<Int>.toRegex(): Regex {
        return joinToString("(\\.*[\\.?]\\.*)", "^[\\.?]*", "[\\.?]*$") { "([?]*[#?]{$it}[?]*)" }.toRegex()
    }
}

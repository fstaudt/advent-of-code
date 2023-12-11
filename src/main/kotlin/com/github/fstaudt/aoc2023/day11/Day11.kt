package com.github.fstaudt.aoc2023.day11

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import kotlin.math.abs

fun main() {
    Day11().run()
}

class Day11(fileName: String = "day_11.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = sumShortestPathLengths(2)
    override fun part2() = sumShortestPathLengths(1000000)

    fun sumShortestPathLengths(factor: Long): Long {
        val space = input.toSpace()
        val longLines = input.mapIndexedNotNull { index, line -> index.takeIf { line.none { point -> point == '#' } } }
        var longColumns = emptyList<Int>()
        for (i in 0..<input[0].length) {
            if (input.none { it[i] == '#' }) {
                longColumns = longColumns + listOf(i)
            }
        }
        return space.flatten().filter { it.galaxy }.let { galaxies ->
            galaxies.flatMap { g1 ->
                galaxies.filter { it != g1 }.map { g2 ->
                    abs(g1.line - g2.line) + abs(g1.column - g2.column) +
                            (longLines.count { g1.line < it && it < g2.line || g2.line < it && it < g1.line } * (factor - 1)) +
                            (longColumns.count { g1.column < it && it < g2.column || g2.column < it && it < g1.column } * (factor - 1))
                }
            }.sumOf { it } / 2
        }
    }

    private fun List<String>.toSpace(): List<List<PointInSpace>> {
        return mapIndexed { line, s ->
            s.mapIndexed { column, point -> PointInSpace(point == '#', line, column) }
        }
    }

    data class PointInSpace(val galaxy: Boolean, val line: Int, val column: Int)
}

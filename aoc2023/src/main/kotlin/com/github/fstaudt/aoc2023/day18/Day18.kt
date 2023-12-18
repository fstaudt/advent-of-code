package com.github.fstaudt.aoc2023.day18

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc2023.day18.Day18.Direction.DOWN
import com.github.fstaudt.aoc2023.day18.Day18.Direction.UP

fun main() {
    Day18().run()
}

class Day18(fileName: String = "day_18.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = countLagoonSize()

    override fun part2() = 0L

    private fun countLagoonSize(): Long {
        val digs = input.map { line ->
            line.split(" ").let { Dig(Direction.of(it[0].first()), it[1].toInt(), it[2]) }
        }
        var currentSpot = Spot(0, 0, true)
        val terrain = mutableMapOf<Int, MutableMap<Int, Spot>>()
        (terrain[0] ?: mutableMapOf<Int, Spot>().also { terrain[0] = it })[0] = currentSpot
        digs.forEach { dig ->
            for (i in 1..dig.size) {
                val lineKey = currentSpot.line + dig.direction.lineDiff
                val columnKey = currentSpot.column + dig.direction.columnDiff
                val line = terrain[lineKey] ?: mutableMapOf<Int, Spot>().also { terrain[lineKey] = it }
                val spot = line[columnKey] ?: Spot(lineKey, columnKey).also { line[columnKey] = it }
                spot.trench = true
                currentSpot = spot
            }
        }
        val minColumn = terrain.values.flatMap { it.keys }.min()
        val maxColumn = terrain.values.flatMap { it.keys }.max()
        for (i in terrain.keys) {
            val line = terrain[i]!!
            for (j in (minColumn..maxColumn)) {
                line[j] ?: Spot(i, j).also { line[j] = it }
            }
        }
        for (i in terrain.keys.sorted()) {
            var lagoon = false
            var trenchDirection: Direction? = null
            for (j in (minColumn..maxColumn)) {
                terrain[i]!![j]!!.lagoon = lagoon || terrain[i]!![j]!!.trench
                if (terrain[i]!![j]!!.trench) {
                    if (terrain[i]!![j - 1]?.trench != true) {
                        if (terrain[i - 1]?.get(j)?.trench != true) {
                            trenchDirection = DOWN
                        } else if (terrain[i + 1]?.get(j)?.trench != true) {
                            trenchDirection = UP
                        } else {
                            lagoon = !lagoon
                        }
                    } else {
                        if (terrain[i - 1]?.get(j)?.trench == true) {
                            if (trenchDirection == DOWN) {
                                lagoon = !lagoon
                            }
                            trenchDirection = null
                        } else if (terrain[i + 1]?.get(j)?.trench == true) {
                            if (trenchDirection == UP) {
                                lagoon = !lagoon
                            }
                            trenchDirection = null
                        }
                    }
                }
            }
        }
        return terrain.values.sumOf { line -> line.values.count { it.lagoon }.toLong() }
    }

    private enum class Direction(val char: Char, val lineDiff: Int, val columnDiff: Int) {
        UP('U', -1, 0),
        DOWN('D', 1, 0),
        RIGHT('R', 0, 1),
        LEFT('L', 0, -1),
        ;

        companion object {
            fun of(char: Char) = Direction.entries.first { it.char == char }
        }
    }

    private data class Dig(val direction: Direction, val size: Int, val color: String)
    private data class Spot(val line: Int, val column: Int, var trench: Boolean = false, var lagoon: Boolean = false)
}

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

    override fun part2() = countLagoonSizeWithColors()

    private fun countLagoonSize(): Long {
        val digs = input.map { line ->
            line.split(" ").let { Dig(Direction.of(it[0].first()), it[1].toInt()) }
        }
        return countLagoonSizeFor(digs)
    }

    private fun countLagoonSizeWithColors(): Long {
        val digs = input.map { line ->
            line.split(" ")[2].let {
                Dig(Direction.entries[it[7].digitToInt()], it.substring(1, 7).toHexa())
            }
        }
        return countLagoonSizeFor(digs)
    }

    fun countLagoonSizeFor(digs: List<Dig>): Long {
        var currentSpot = Spot(0, 0)
        val terrain = mutableMapOf<Long, MutableMap<Long, Spot>>()
        val flipped = mutableMapOf<Long, MutableMap<Long, Spot>>()
        (terrain[0] ?: mutableMapOf<Long, Spot>().also { terrain[0L] = it })[0L] = currentSpot
        (flipped[0] ?: mutableMapOf<Long, Spot>().also { flipped[0L] = it })[0L] = currentSpot
        digs.forEach { dig ->
            currentSpot.directions += dig.direction
            val lineKey = currentSpot.line + dig.size * dig.direction.lineDiff
            val columnKey = currentSpot.column + dig.size * dig.direction.columnDiff

            currentSpot = (terrain[lineKey]?.get(columnKey) ?: Spot(lineKey, columnKey)).also {
                it.directions += dig.direction.backward()
            }
            (terrain[lineKey] ?: mutableMapOf<Long, Spot>().also { terrain[lineKey] = it })[columnKey] = currentSpot
            (flipped[columnKey] ?: mutableMapOf<Long, Spot>().also { flipped[columnKey] = it })[lineKey] = currentSpot
        }
        val lineIndices = terrain.keys.sorted()
        var sum = 0L
        terrain[lineIndices[0]]!!.keys.chunked(2).forEach { sum += it[1] - it[0] + 1 }
        var previousLineIndex = lineIndices[0]
        for (lineIndex in lineIndices.drop(1)) {
            // previous intermediate lines
            if (previousLineIndex != lineIndex - 1) {
                flipped.values.sortedBy { it.values.first().column }.flatMap { col ->
                    col.entries.sortedBy { it.key }.chunked(2)
                        .filter { it[0].key < lineIndex - 1 && lineIndex - 1 < it[1].key }.map { it[0].value.column }
                }.chunked(2).forEach {
                    sum += (it[1] - it[0] + 1) * (lineIndex - previousLineIndex - 1)
                }
            }
            previousLineIndex = lineIndex
            // current line
            var lagoon = false
            var trenchDirection: Direction? = null
            var start: Long? = null
            (flipped.values.sortedBy { it.values.first().column }.flatMap { col ->
                col.entries.sortedBy { it.key }.chunked(2)
                    .filter { it[0].key < lineIndex && lineIndex < it[1].key }.map { it[0].value }
            } + terrain[lineIndex]!!.values).sortedBy { it.column }.forEach { spot ->
                when {
                    spot.line != lineIndex -> {
                        if (lagoon) {
                            sum += spot.column - (start!!) + 1
                            start = null
                        } else start = spot.column
                        lagoon = !lagoon
                    }

                    trenchDirection == null -> {
                        trenchDirection = spot.directions.first { it == UP || it == DOWN }
                        start = start ?: spot.column
                    }

                    trenchDirection == spot.directions.first { it == UP || it == DOWN } -> {
                        if (!lagoon) {
                            sum += spot.column - (start!!) + 1
                            start = null
                        }
                        trenchDirection = null
                    }

                    else -> {
                        trenchDirection = null
                        if (lagoon) {
                            sum += spot.column - (start!!) + 1
                            start = null
                        }
                        lagoon = !lagoon
                    }
                }
            }
        }
        return sum
    }

    private fun String.toHexa() = fold(0) { hexa, c -> hexa * 16 + c.toHexa() }

    private fun Char.toHexa() = when (this) {
        '1', '2', '3', '4', '5', '6', '7', '8', '9' -> digitToInt()
        'a' -> 10
        'b' -> 11
        'c' -> 12
        'd' -> 13
        'e' -> 14
        'f' -> 15
        else -> 0
    }

    enum class Direction(val char: Char, val lineDiff: Int, val columnDiff: Int) {
        RIGHT('R', 0, 1),
        DOWN('D', 1, 0),
        LEFT('L', 0, -1),
        UP('U', -1, 0),
        ;

        fun backward() = when (this) {
            RIGHT -> LEFT
            DOWN -> UP
            LEFT -> RIGHT
            UP -> DOWN
        }

        companion object {
            fun of(char: Char) = Direction.entries.first { it.char == char }
        }
    }

    data class Dig(val direction: Direction, val size: Int)
    private data class Spot(val line: Long, val column: Long, val directions: MutableSet<Direction> = mutableSetOf())
}

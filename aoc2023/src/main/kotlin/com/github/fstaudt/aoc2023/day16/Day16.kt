package com.github.fstaudt.aoc2023.day16

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc2023.day16.Day16.Direction.*
import com.github.fstaudt.aoc2023.day16.Day16.TileType.*

fun main() {
    Day16().run()
}

class Day16(fileName: String = "day_16.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = countEnergizedTiles(0, 0, RIGHT)
    override fun part2() = maxCountEnergizedTiles()

    private fun maxCountEnergizedTiles(): Long {
        val tiles = input.map { line -> line.map { Tile(TileType.of(it)) } }
        var max = 0L
        for (i in tiles.indices) {
            max = max.coerceAtLeast(countEnergizedTiles(i, 0, RIGHT))
            max = max.coerceAtLeast(countEnergizedTiles(i, tiles[0].size - 1, LEFT))
        }
        for (j in tiles[0].indices) {
            max = max.coerceAtLeast(countEnergizedTiles(0, j, DOWN))
            max = max.coerceAtLeast(countEnergizedTiles(tiles.size - 1, j, RIGHT))
        }
        return max
    }

    private fun countEnergizedTiles(line: Int, column: Int, direction: Direction): Long {
        val tiles = input.map { l -> l.map { Tile(TileType.of(it)) } }
        tiles.energize(line, column, direction)
        return tiles.sumOf { l -> l.count { it.energized }.toLong() }
    }

    private fun List<List<Tile>>.energize(line: Int, column: Int, direction: Direction) {
        if (line < 0 || line >= size) return
        if (column < 0 || column >= get(0).size) return
        if (this[line][column].processedDirections.contains(direction)) return

        this[line][column].energized = true
        this[line][column].processedDirections += direction

        when (this[line][column].type) {
            SPACE -> energize(line + direction.lineDiff, column + direction.columnDiff, direction)
            UP_MIRROR -> when (direction) {
                RIGHT -> UP
                LEFT -> DOWN
                UP -> RIGHT
                DOWN -> LEFT
            }.let { energize(line + it.lineDiff, column + it.columnDiff, it) }

            DOWN_MIRROR -> when (direction) {
                RIGHT -> DOWN
                LEFT -> UP
                UP -> LEFT
                DOWN -> RIGHT
            }.let { energize(line + it.lineDiff, column + it.columnDiff, it) }

            HORIZONTAL_SPLITTER -> {
                when (direction) {
                    LEFT, RIGHT -> energize(line + direction.lineDiff, column + direction.columnDiff, direction)
                    UP, DOWN -> {
                        LEFT.also { energize(line + it.lineDiff, column + it.columnDiff, it) }
                        RIGHT.also { energize(line + it.lineDiff, column + it.columnDiff, it) }
                    }
                }
            }

            VERTICAL_SPLITTER -> {
                when (direction) {
                    UP, DOWN -> energize(line + direction.lineDiff, column + direction.columnDiff, direction)
                    LEFT, RIGHT -> {
                        UP.also { energize(line + it.lineDiff, column + it.columnDiff, it) }
                        DOWN.also { energize(line + it.lineDiff, column + it.columnDiff, it) }
                    }
                }
            }
        }
    }

    private data class Tile(
        val type: TileType,
        var energized: Boolean = false,
        var processedDirections: Set<Direction> = emptySet()
    )

    enum class TileType(val char: Char) {
        SPACE('.'),
        UP_MIRROR('/'),
        DOWN_MIRROR('\\'),
        HORIZONTAL_SPLITTER('-'),
        VERTICAL_SPLITTER('|'),
        ;

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }

    private enum class Direction(val lineDiff: Int, val columnDiff: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        RIGHT(0, 1),
        LEFT(0, -1),
    }
}

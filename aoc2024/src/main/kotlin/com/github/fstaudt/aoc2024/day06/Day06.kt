package com.github.fstaudt.aoc2024.day06

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc2024.day06.Day06.Direction.*
import com.github.fstaudt.aoc2024.day06.Day06.PositionType.*

fun main() {
    Day06().run()
}

class Day06(fileName: String = "day_06.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val room = Room(input.mapIndexed { i, line -> line.mapIndexed { j, it -> Position.of(it, i, j) } })

    override fun part1(): Long {
        room.guard().visitRoom()
        room.print()
        return room.positions().count { it.visited() }.toLong()
    }

    override fun part2(): Long {
        var variantsWithLoop = 0L
        room.guard().visitRoom()
        room.positions().filter { it.visited() && it.type != GUARD }.forEach { position ->
            val roomVariant = room.variantWithObstacleOn(position)
            roomVariant.guard().visitRoom()
            if (roomVariant.loop) variantsWithLoop++
        }
        return variantsWithLoop
    }

    data class Room(val positions: Matrix<Position>, var loop: Boolean = false) {
        fun guard() = Guard(this, positions().first { it.type == GUARD }, UP)
        fun position(i: Int, j: Int) = runCatching { positions[i][j] }.getOrNull()
        fun positions() = positions.flatMap { it }
        fun variantWithObstacleOn(position: Position): Room {
            return Room(positions.map {
                it.map { p ->
                    Position(if (p == position) OBSTACLE else p.type, p.i, p.j)
                }
            })
        }

        fun print() {
            positions.forEach { line ->
                line.forEach { print(if (it.visited() && it.type != GUARD) 'X' else it.type.char) }
                println()
            }
            println()
        }
    }

    data class Guard(var room: Room, var position: Position, var direction: Direction) {
        fun turnRight() {
            direction = when (direction) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }

        fun visitRoom() {
            position.visited.put(direction, true)
            while (true) {
                val nextPosition = room.position(position.i + direction.di, position.j + direction.dj)
                when {
                    nextPosition == null -> break
                    nextPosition.visited[direction] == true -> {
                        room.loop = true
                        break
                    }

                    nextPosition.type == OBSTACLE -> turnRight()
                    else -> position = nextPosition.also { it.visited.put(direction, true) }
                }
            }
        }
    }

    data class Position(
        val type: PositionType, val i: Int, val j: Int,
        var visited: MutableMap<Direction, Boolean> = mutableMapOf()
    ) {
        companion object {
            fun of(char: Char, i: Int, j: Int) = Position(PositionType.of(char), i, j)
        }

        fun visited() = visited.isNotEmpty()
        override fun hashCode() = 31 * i + j
        override operator fun equals(other: Any?): Boolean {
            return (other as? Position)?.let { it.i == i && it.j == j } == true
        }
    }

    enum class PositionType(val char: Char) {
        OBSTACLE('#'),
        FREE_SPOT('.'),
        GUARD('^');

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }

    enum class Direction(val di: Int, val dj: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        RIGHT(0, 1),
        LEFT(0, -1);
    }
}
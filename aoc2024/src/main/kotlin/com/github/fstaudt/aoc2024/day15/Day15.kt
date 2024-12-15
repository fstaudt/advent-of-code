package com.github.fstaudt.aoc2024.day15

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.print
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf
import com.github.fstaudt.aoc2024.day15.Day15.Instruction.*
import com.github.fstaudt.aoc2024.day15.Day15.Type.*

fun main() {
    Day15().run()
}

class Day15(fileName: String = "day_15.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val instructions = input.dropWhile { it.isNotEmpty() }.flatMap { it.map { Instruction.of(it) } }
    fun warehouse() =
        Warehouse(input.takeWhile { it.isNotEmpty() }.toMatrixOf { Position(it.i, it.j, Type.of(it.char)) })

    override fun part1(): Long {
        val warehouse = warehouse()
        var robot = warehouse.positions.flatten().first { it.type == ROBOT }
        instructions.forEach { instruction ->
            if (robot.canMove(instruction, warehouse)) {
                robot.move(instruction, warehouse)
                robot = warehouse.position(robot.i + instruction.di, robot.j + instruction.dj)!!
            }
        }
        warehouse.print()
        return warehouse.positions.flatten().filter { it.type == BOX }.sumOf { it.i * 100L + it.j }
    }

    override fun part2(): Long {
        val warehouse = warehouse().wider()
        var robot = warehouse.positions.flatten().first { it.type == ROBOT }
        instructions.forEach { instruction ->
            if (robot.canMoveBigBoxes(instruction, warehouse)) {
                robot.moveBigBoxes(instruction, warehouse)
                robot = warehouse.position(robot.i + instruction.di, robot.j + instruction.dj)!!
            }
        }
        warehouse.print()
        return warehouse.positions.flatten().filter { it.type == LEFT_BOX }.sumOf { it.i * 100L + it.j }
    }

    data class Warehouse(val positions: Matrix<Position>) {
        fun position(i: Int, j: Int) = positions.getOrNull(i)?.getOrNull(j)
        fun print() = positions.print { it.type.char }
        fun wider(): Warehouse {
            return Warehouse(positions.map { line ->
                line.flatMap {
                    when (it.type) {
                        FREE -> listOf(Position(it.i, 2 * it.j, FREE), Position(it.i, 2 * it.j + 1, FREE))
                        BOX -> listOf(Position(it.i, 2 * it.j, LEFT_BOX), Position(it.i, 2 * it.j + 1, RIGHT_BOX))
                        WALL -> listOf(Position(it.i, 2 * it.j, WALL), Position(it.i, 2 * it.j + 1, WALL))
                        ROBOT -> listOf(Position(it.i, 2 * it.j, ROBOT), Position(it.i, 2 * it.j + 1, FREE))
                        else -> listOf(it)
                    }
                }
            })
        }
    }

    data class Position(val i: Int, val j: Int, var type: Type) {
        fun canMove(instruction: Instruction, warehouse: Warehouse): Boolean {
            val nextPosition = warehouse.position(i + instruction.di, j + instruction.dj)
            return when (nextPosition?.type) {
                FREE -> true
                BOX -> nextPosition.canMove(instruction, warehouse)
                else -> false
            }
        }

        fun move(instruction: Instruction, warehouse: Warehouse) {
            val nextPosition = warehouse.position(i + instruction.di, j + instruction.dj)
            when (nextPosition?.type) {
                FREE -> {
                    nextPosition.type = type
                    type = FREE
                }

                BOX -> {
                    nextPosition.move(instruction, warehouse)
                    nextPosition.type = type
                    type = FREE
                }

                else -> Unit
            }
        }

        fun canMoveBigBoxes(instruction: Instruction, warehouse: Warehouse): Boolean {
            val next = warehouse.position(i + instruction.di, j + instruction.dj)
            return when (next?.type) {
                FREE -> true
                LEFT_BOX -> {
                    if (instruction == UP || instruction == DOWN) {
                        val right = warehouse.position(next.i + RIGHT.di, next.j + RIGHT.dj)!!
                        next.canMoveBigBoxes(instruction, warehouse) &&
                            right.canMoveBigBoxes(instruction, warehouse)
                    } else next.canMoveBigBoxes(instruction, warehouse)
                }

                RIGHT_BOX -> {
                    if (instruction == UP || instruction == DOWN) {
                        val left = warehouse.position(next.i + LEFT.di, next.j + LEFT.dj)!!
                        next.canMoveBigBoxes(instruction, warehouse) &&
                            left.canMoveBigBoxes(instruction, warehouse)
                    } else next.canMoveBigBoxes(instruction, warehouse)
                }

                else -> false
            }
        }

        fun moveBigBoxes(instruction: Instruction, warehouse: Warehouse) {
            val next = warehouse.position(i + instruction.di, j + instruction.dj)
            when (next?.type) {
                FREE -> {
                    next.type = type
                    type = FREE
                }

                LEFT_BOX -> {
                    next.moveBigBoxes(instruction, warehouse)
                    next.type = type
                    type = FREE
                    if (instruction == UP || instruction == DOWN) {
                        val right = warehouse.position(next.i + RIGHT.di, next.j + RIGHT.dj)!!
                        right.moveBigBoxes(instruction, warehouse)
                        right.type = FREE
                    }
                }

                RIGHT_BOX -> {
                    next.moveBigBoxes(instruction, warehouse)
                    next.type = type
                    type = FREE
                    if (instruction == UP || instruction == DOWN) {
                        val left = warehouse.position(next.i + LEFT.di, next.j + LEFT.dj)!!
                        left.moveBigBoxes(instruction, warehouse)
                        left.type = FREE
                    }
                }

                else -> Unit
            }
        }
    }

    enum class Type(val char: Char) {
        FREE('.'),
        BOX('O'),
        WALL('#'),
        ROBOT('@'),
        LEFT_BOX('['),
        RIGHT_BOX(']'),
        ;

        companion object {
            fun of(char: Char) = Type.entries.first { it.char == char }
        }
    }

    enum class Instruction(val char: Char, val di: Int, val dj: Int) {
        RIGHT('>', 0, 1),
        DOWN('v', 1, 0),
        LEFT('<', 0, -1),
        UP('^', -1, 0),
        ;

        companion object {
            fun of(char: Char) = Instruction.entries.first { it.char == char }
        }
    }
}
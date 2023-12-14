package com.github.fstaudt.aoc2023.day14

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.MatrixExtensions.flipMatrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.tiltClockwise
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc2023.day14.Day14.Type.*

fun main() {
    Day14().run()
}

class Day14(fileName: String = "day_14.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = sumRockLoads()

    override fun part2() = sumRockLoadsAfterOneBillionSpin()

    private fun sumRockLoads(): Long {
        val platform = input.toPlatform()
        platform.moveRoundedRocks()
        return platform.sumRockLoads()
    }

    private fun sumRockLoadsAfterOneBillionSpin(): Long {
        val cycles = 1_000_000_000
        var platform = input.toPlatform()
        val platformsByInput = mutableMapOf<String, Int>()
        val rockLoadsByCycle = mutableMapOf<Int, Long>()
        var cycle = 0
        while (!platformsByInput.containsKey(platform.toString()) && cycle < cycles) {
            platformsByInput[platform.toString()] = cycle
            rockLoadsByCycle[cycle] = platform.sumRockLoads()
            platform = platform.spin()
            cycle++
        }
        val init = platformsByInput[platform.toString()]!!
        val loop = cycle - init
        val rest = (cycles - init).rem(loop)
        return rockLoadsByCycle[init + rest]!!
    }

    private fun Platform.spin(): Platform {
        val northPlatform = also { moveRoundedRocks() }
        val westPlatform = northPlatform.tiltClockwise().also { it.moveRoundedRocks() }
        val southPlatform = westPlatform.tiltClockwise().also { it.moveRoundedRocks() }
        val eastPlatform = southPlatform.tiltClockwise().also { it.moveRoundedRocks() }
        return eastPlatform.tiltClockwise()
    }

    private fun List<String>.toPlatform(): Platform {
        return Platform(map { line -> line.map { char -> Position(Type.of(char)) } })
    }

    data class Platform(val lines: List<List<Position>>) {
        fun moveRoundedRocks() {
            val flippedLines = lines.flipMatrix()
            for (l in flippedLines.indices) {
                var target: Int? = null
                for (p in (0..<flippedLines[l].size)) {
                    when (flippedLines[l][p].type) {
                        SPACE -> target = target ?: p
                        ROCK -> target = null
                        ROUNDED_ROCK ->
                            if (target != null) {
                                flippedLines[l][target].type = ROUNDED_ROCK
                                flippedLines[l][p].type = SPACE
                                target += 1
                            }
                    }
                }
            }
        }

        fun sumRockLoads(): Long {
            return lines.flipMatrix().map { line ->
                line.mapIndexedNotNull { index, position ->
                    (line.size - index).takeIf { position.type == ROUNDED_ROCK }
                }.sumOf { it.toLong() }
            }.sumOf { it }
        }

        fun tiltClockwise() = Platform(lines.tiltClockwise())
        override fun toString() = lines.joinToString("\n") { line -> line.joinToString("") { "${it.type.char}" } }
    }

    data class Position(var type: Type)
    enum class Type(val char: Char) {
        ROCK('#'),
        ROUNDED_ROCK('O'),
        SPACE('.');

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }
}

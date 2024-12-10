package com.github.fstaudt.aoc2024.day10

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf

fun main() {
    Day10().run()
}

class Day10(fileName: String = "day_10.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val map = TopographicMap(input.toMatrixOf { Location(it.i, it.j, it.char.digitToInt()) })

    override fun part1(): Long {
        return map.locations().filter { it.height == 0 }.sumOf {
            map.countTrailendsFrom(it)
        }
    }

    override fun part2(): Long {
        return map.locations().filter { it.height == 0 }.sumOf {
            map.countTrailsFrom(it)
        }
    }

    data class Location(val i: Int, val j: Int, val height: Int, var trailend: Boolean = false)
    data class TopographicMap(val locations: Matrix<Location>) {
        fun location(i: Int, j: Int) = locations.getOrNull(i)?.getOrNull(j)
        fun locations() = locations.flatMap { it }
        fun countTrailendsFrom(location: Location): Long {
            countTrailsFrom(location)
            return locations().filter { it.trailend }.map { it.also { it.trailend = false } }.count().toLong()
        }

        fun countTrailsFrom(step: Location): Long {
            return if (step.height == 9) {
                step.trailend = true
                1L
            }else {
                Direction.entries.sumOf { dir ->
                    val nextStep = location(step.i + dir.di, step.j + dir.dj)
                    if (nextStep?.height == step.height + 1)
                        countTrailsFrom(nextStep)
                    else 0L
                }
            }
        }
    }

    enum class Direction(val di: Int, val dj: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        RIGHT(0, 1),
        LEFT(0, -1);
    }
}
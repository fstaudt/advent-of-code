package com.github.fstaudt.aoc2025.day12

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.MatchResultExtensions.group
import com.github.fstaudt.aoc.shared.Matrix

fun main() {
    Day12().run()
}

class Day12(fileName: String = "day_12.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val shapes = input.take(30).chunked(5).map { Shape.from(it) }
    private val regions = input.drop(30).map { Region.from(it, shapes) }

    override fun part1() = regions.count { it.dummyFitRatio <= 100 }.toLong()

    override fun part2() = 0L

    private data class Shape(val pattern: Matrix<Boolean>) {
        companion object {
            fun from(input: List<String>): Shape {
                val pattern = input.drop(1).dropLast(1).map { it.map { c -> c == '#' } }
                return Shape(pattern)
            }
        }
    }

    private data class Region(val width: Int, val height: Int, val shapes: List<Pair<Int, Shape>>) {
        companion object {
            val REGEX = "^(?<width>\\d+)x(?<height>\\d+):(?<quantities>( \\d+)+)$".toRegex()
            fun from(input: String, shapes: List<Shape>): Region {
                val match = REGEX.find(input)!!
                val width = match.group("width").toInt()
                val height = match.group("height").toInt()
                val quantities = match.group("quantities").trim().split(" ").map { it.toInt() }
                return Region(width, height, quantities.mapIndexed { i, q -> q to shapes[i] })
            }
        }

        val dummyFitRatio = shapes.sumOf { it.first } * 100.0 / ((width / 3) * (height / 3))
    }
}
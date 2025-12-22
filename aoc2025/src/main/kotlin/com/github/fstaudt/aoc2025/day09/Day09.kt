package com.github.fstaudt.aoc2025.day09

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import kotlin.collections.flatMapIndexed
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day09().run()
}

class Day09(fileName: String = "day_09.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val locations = input.map { Location(it.split(",")) }
    private val mappedLocations = locations.mapped()
    private val borders = mappedLocations.toBorders()
    private val rectangles = mappedLocations.toRectangles()

    override fun part1() = rectangles.maxOf { it.area() }

    override fun part2(): Long = rectangles.maxOf { if (it.isInside(borders)) it.area() else 0L }

    private fun List<Location>.mapped(): List<MappedLocation> {
        val xMappings = mappingsBy { it.x }
        val yMappings = mappingsBy { it.y }
        return map { MappedLocation(xMappings[it.x]!!, yMappings[it.y]!!, it) }
    }

    private fun List<Location>.mappingsBy(by: (Location) -> Int): Map<Int, Int> {
        return asSequence().map { by(it) }.distinct().sorted().mapIndexed { i, m -> m to 2 * i + 1 }.associate { it }
    }

    private fun List<MappedLocation>.toRectangles(): List<Rectangle> {
        return flatMapIndexed { i, from -> take(i).map { Rectangle(from, it) } }
    }

    private fun List<MappedLocation>.toBorders(): Map<Int, Map<Int, Boolean>> {
        return mutableMapOf<Int, MutableMap<Int, Boolean>>().also { borders ->
            plus(first()).windowed(2).forEach { (from, until) ->
                val mxRange = min(from.mx, until.mx)..max(from.mx, until.mx)
                val myRange = min(from.my, until.my)..max(from.my, until.my)
                mxRange.forEach { mx ->
                    myRange.forEach { my ->
                        borders.getOrPut(mx) { mutableMapOf() }[my] = true
                    }
                }
            }
        }
    }

    private data class Location(var positions: List<String>) {
        val x = positions[0].toInt()
        val y = positions[1].toInt()
    }

    private data class MappedLocation(val mx: Int, val my: Int, val location: Location)

    private data class Rectangle(val from: MappedLocation, val until: MappedLocation) {
        fun area(): Long {
            return (abs(until.location.x - from.location.x) + 1).toLong() *
                    (abs(until.location.y - from.location.y) + 1)
        }

        fun isInside(borders: Map<Int, Map<Int, Boolean>>): Boolean {
            val xMin = min(from.mx, until.mx)
            val xMax = max(from.mx, until.mx)
            val yMin = min(from.my, until.my)
            val yMax = max(from.my, until.my)
            if (xMin == xMax || yMin == yMax) return false
            (xMin..xMax).filter { it % 2 == 0 }.forEach { mx ->
                if (!(mx to yMin + 1).isInside(borders)) return false
                if (!(mx to yMax - 1).isInside(borders)) return false
            }
            (yMin..yMax).filter { it % 2 == 0 }.forEach { my ->
                if (!(xMin + 1 to my).isInside(borders)) return false
                if (!(xMax - 1 to my).isInside(borders)) return false
            }
            return true
        }

        private fun Pair<Int, Int>.isInside(borders: Map<Int, Map<Int, Boolean>>): Boolean {
            return borders[first]?.keys?.count { it < second }?.let { it % 2 != 0 } ?: false
        }
    }
}

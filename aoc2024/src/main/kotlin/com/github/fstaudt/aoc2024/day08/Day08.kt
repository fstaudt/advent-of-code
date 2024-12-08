package com.github.fstaudt.aoc2024.day08

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix

fun main() {
    Day08().run()
}

class Day08(fileName: String = "day_08.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val city = City(input.mapIndexed { i, line -> line.mapIndexed { j, char -> Location(i, j, char) } })

    override fun part1(): Long {
        city.markAntiNodesWith { a1: Location, a2: Location ->
            val dx = a2.x - a1.x
            val dy = a2.y - a1.y
            city.location(a2.x + dx, a2.y + dy)?.antiNode = true
            city.location(a1.x - dx, a1.y - dy)?.antiNode = true
        }
        return city.locations().count { it.antiNode }.toLong()
    }

    override fun part2(): Long {
        city.antennas().forEach { it.antiNode = true }
        city.markAntiNodesWith { a1: Location, a2: Location ->
            val dx = a2.x - a1.x
            val dy = a2.y - a1.y
            var h1 = 1
            while (city.location(a2.x + h1 * dx, a2.y + h1 * dy)?.also { it.antiNode = true } != null) h1++
            var h2 = 1
            while (city.location(a1.x - h2 * dx, a1.y - h2 * dy)?.also { it.antiNode = true } != null) h2++
        }
        return city.locations().count { it.antiNode }.toLong()
    }

    data class Location(val x: Int, val y: Int, val frequency: Char, var antiNode: Boolean = false)
    data class City(val locations: Matrix<Location>) {
        fun location(x: Int, y: Int) = runCatching { locations[x][y] }.getOrNull()
        fun locations() = locations.flatMap { it }
        fun antennas() = locations().filter { it.frequency != '.' }
        fun markAntiNodesWith(markFor: (Location, Location) -> Unit) {
            antennas().groupBy { it.frequency }.forEach { (_, antennaGroup) ->
                antennaGroup.forEachIndexed { i, a1 ->
                    antennaGroup.drop(i + 1).forEach { a2 ->
                        markFor(a1, a2)
                    }
                }
            }
        }
    }
}

package com.github.fstaudt.aoc2024.day08

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf

fun main() {
    Day08().run()
}

class Day08(fileName: String = "day_08.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val city = City(input.toMatrixOf{ Location(it.i, it.j, it.char) })

    override fun part1(): Long {
        city.markAntiNodesWith { a1: Location, a2: Location ->
            val dx = a2.i - a1.i
            val dy = a2.j - a1.j
            city.location(a2.i + dx, a2.j + dy)?.antiNode = true
            city.location(a1.i - dx, a1.j - dy)?.antiNode = true
        }
        return city.locations().count { it.antiNode }.toLong()
    }

    override fun part2(): Long {
        city.antennas().forEach { it.antiNode = true }
        city.markAntiNodesWith { a1: Location, a2: Location ->
            val di = a2.i - a1.i
            val dj = a2.j - a1.j
            var h1 = 1
            while (city.location(a2.i + h1 * di, a2.j + h1 * dj)?.also { it.antiNode = true } != null) h1++
            var h2 = 1
            while (city.location(a1.i - h2 * di, a1.j - h2 * dj)?.also { it.antiNode = true } != null) h2++
        }
        return city.locations().count { it.antiNode }.toLong()
    }

    data class Location(val i: Int, val j: Int, val frequency: Char, var antiNode: Boolean = false)
    data class City(val locations: Matrix<Location>) {
        fun location(i: Int, j: Int) = runCatching { locations[i][j] }.getOrNull()
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

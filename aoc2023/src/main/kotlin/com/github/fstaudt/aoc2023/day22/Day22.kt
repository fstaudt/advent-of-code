package com.github.fstaudt.aoc2023.day22

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import kotlin.math.min

fun main() {
    Day22().run()
}

class Day22(fileName: String = "day_22.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = countSafelyDisintegratableBricks()
    override fun part2() = sumFallenBricksAfterDisintegration()

    private fun sumFallenBricksAfterDisintegration(): Long {
        val snapshot = input.toSnapshot().sortedBy { it.from.z }
        val supportedBricks = supportedBricks(snapshot)
        return snapshot.sumOf { brick ->
            supportedBricks.foldIndexed(listOf(brick.index)) { index, fallen, supported ->
                if (supported.isNotEmpty() && supported.all { fallen.contains(it) }) fallen + index else fallen
            }.size - 1
        }.toLong()
    }

    private fun countSafelyDisintegratableBricks(): Long {
        val snapshot = input.toSnapshot().sortedBy { it.from.z }
        val supportedBricks = supportedBricks(snapshot)
        val singleSupports = supportedBricks.filter { it.distinct().size == 1 }.flatten().distinct()
        return (input.size - singleSupports.size).toLong()
    }

    private fun supportedBricks(snapshot: List<Position>): List<MutableList<Int>> {
        val pile = mutableMapOf(1 to XYLayer()) // z, x, y
        snapshot.forEachIndexed { index, brick ->
            var z = min(pile.keys.max(), brick.from.z)
            while (z > 1 && (brick.toXY().all { pile[z - 1]!!.grid[it.x]?.get(it.y) == null })) {
                z--
            }
            brick.toXY().forEach { xy ->
                brick.toZDiff().forEach { zDiff ->
                    pile.getOrPut(z + zDiff) { XYLayer() }.grid.getOrPut(xy.x) { mutableMapOf() }[xy.y] = index
                }
            }
            val zMax = pile.keys.max()
            if (pile[zMax]!!.grid.isNotEmpty()) {
                pile[zMax + 1] = XYLayer()
            }
        }
        val supportedBy = snapshot.map { mutableListOf<Int>() }
        pile.keys.forEach { z ->
            pile[z]!!.grid.forEach { xy ->
                val x = xy.key
                xy.value.forEach {
                    val y = it.key
                    val brick = it.value
                    val brickBelow = pile[z - 1]?.grid?.get(x)?.get(y)
                    if (brickBelow != null && brickBelow != brick) {
                        supportedBy[brick] += brickBelow
                    }
                }
            }
        }
        return supportedBy
    }

    private fun List<String>.toSnapshot() = mapIndexed { index, it ->
        it.split("~").let { xyz -> Position(index, xyz[0].toCoordinate(), xyz[1].toCoordinate()) }
    }

    private fun String.toCoordinate() = split(",").let { Coordinate(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
    data class Position(val index: Int, val from: Coordinate, val until: Coordinate) {
        fun toXY() = (from.x..until.x).flatMap { x -> (from.y..until.y).map { y -> XY(x, y) } }
        fun toZDiff() = (0..until.z - from.z)
        override fun toString() = "${from}~${until}"
    }

    data class XYLayer(val grid: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()) {
        override fun toString(): String {
            val xMax = grid.keys.maxOrNull() ?: 0
            val yMax = grid.values.flatMap { it.keys }.maxOrNull() ?: 0
            return (0..xMax).joinToString("\n") { x -> (0..yMax).joinToString("") { y -> "${grid[x]?.get(y) ?: "."}" } }
        }
    }

    data class XY(val x: Int, val y: Int)
    data class Coordinate(val x: Int, val y: Int, val z: Int) {
        override fun toString() = "$x,$y,$z"
    }
}

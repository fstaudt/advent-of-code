package com.github.fstaudt.aoc2024.day12

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf

fun main() {
    Day12().run()
}

class Day12(fileName: String = "day_12.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val garden = Garden(input.toMatrixOf { Plot(it.i, it.j, it.char) }).also { it.setPlotRegions() }

    override fun part1() = garden.regions.sumOf { it.area() * it.perimeter() }

    override fun part2() = 0L

    data class Garden(val plots: Matrix<Plot>, val regions: MutableList<Region> = mutableListOf()) {
        fun plot(i: Int, j: Int) = plots.getOrNull(i)?.getOrNull(j)
        fun setPlotRegions() {
            plots.flatten().forEach { plot ->
                if (plot.region == null) {
                    plot.region = Region(plot.plantType, mutableListOf(plot)).also { regions.add(it) }
                }
                propagateRegionFor(plot)
            }
        }

        private fun propagateRegionFor(plot: Plot) {
            Direction.entries.forEach { dir ->
                plot(plot.i + dir.di, plot.j + dir.dj)?.takeIf { it.plantType == plot.plantType }?.let { otherPlot ->
                    if (otherPlot.region == null) {
                        plot.region!!.add(otherPlot)
                        propagateRegionFor(otherPlot)
                    }
                }
            }
        }
    }

    data class Plot(val i: Int, val j: Int, val plantType: Char, var region: Region? = null) {
        override fun toString(): String {
            return "$plantType $i $j"
        }
    }

    data class Region(val plantType: Char, val plots: MutableList<Plot> = mutableListOf()) {
        fun plot(i: Int, j: Int) = plots.firstOrNull { it.i == i && it.j == j }
        fun add(plot: Plot) = plots.add(plot.also { it.region = this })
        fun area() = plots.count()
        fun perimeter() = plots.sumOf { plot ->
            Direction.entries.sumOf { dir ->
                if (plot(plot.i + dir.di, plot.j + dir.dj) == null) 1L else 0
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
package com.github.fstaudt.aoc2024.day12

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf
import com.github.fstaudt.aoc2024.day12.Day12.Direction.*

fun main() {
    Day12().run()
}

class Day12(fileName: String = "day_12.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val garden = Garden(input.toMatrixOf { Plot(it.i, it.j, it.char) }).also { it.setPlotRegions() }

    override fun part1() = garden.regions.sumOf { it.area() * it.perimeter() }

    override fun part2() = garden.regions.sumOf { it.area() * it.sides() }

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

        fun sides(): Long {
            var sides = 0L
            plots.map { it.i }.distinct().forEach { line ->
                sides += hSides(line, UP)
                sides += hSides(line, DOWN)
            }
            plots.map { it.j }.distinct().forEach { line ->
                sides += vSides(line, LEFT)
                sides += vSides(line, RIGHT)
            }

            return sides
        }

        private fun vSides(column: Int, leftRight: Direction): Long {
            var sides = 0L
            var side = false
            plots.filter { it.j == column }.sortedBy { it.i }.forEach { p ->
                if ((plot(p.i + leftRight.di, p.j + leftRight.dj) == null &&
                        plot(p.i + UP.di, p.j + UP.dj) == null) ||
                    (plot(p.i + leftRight.di, p.j + leftRight.dj) == null &&
                        plot(p.i + leftRight.di + UP.di, p.j + leftRight.dj + UP.dj) != null)
                ) {
                    side = true
                }
                if (side == true &&
                    (plot(p.i + leftRight.di, p.j + leftRight.dj) == null &&
                        plot(p.i + DOWN.di, p.j + DOWN.dj) == null) ||
                    (plot(p.i + leftRight.di, p.j + leftRight.dj) == null &&
                        plot(p.i + leftRight.di + DOWN.di, p.j + leftRight.dj + DOWN.dj) != null)
                ) {
                    sides++
                    side = false
                }
            }
            return sides
        }

        private fun hSides(line: Int, upDown: Direction): Long {
            var sides = 0L
            var side = false
            plots.filter { it.i == line }.sortedBy { it.j }.forEach { p ->
                if ((plot(p.i + upDown.di, p.j + upDown.dj) == null &&
                        plot(p.i + LEFT.di, p.j + LEFT.dj) == null) ||
                    (plot(p.i + upDown.di, p.j + upDown.dj) == null &&
                        plot(p.i + upDown.di + LEFT.di, p.j + upDown.dj + LEFT.dj) != null)
                ) {
                    side = true
                }
                if (side == true &&
                    (plot(p.i + upDown.di, p.j + upDown.dj) == null &&
                        plot(p.i + RIGHT.di, p.j + RIGHT.dj) == null) ||
                    (plot(p.i + upDown.di, p.j + upDown.dj) == null &&
                        plot(p.i + upDown.di + RIGHT.di, p.j + upDown.dj + RIGHT.dj) != null)
                ) {
                    sides++
                    side = false
                }
            }
            return sides
        }
    }

    enum class Direction(val di: Int, val dj: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        RIGHT(0, 1),
        LEFT(0, -1);
    }
}
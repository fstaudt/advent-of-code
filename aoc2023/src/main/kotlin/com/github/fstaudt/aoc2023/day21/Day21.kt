package com.github.fstaudt.aoc2023.day21

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day21().run()
}

class Day21(fileName: String = "day_21.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = countGardenPlotsReachedAfter(64)

    override fun part2() = countGardenPlotsReachedOnInfiniteGardenAfter(26501365)

    fun countGardenPlotsReachedAfter(steps: Int): Long {
        val garden = input.toGarden(0, 0)
        var evenOrOdd = 0
        var step = 1
        while (step <= steps) {
            for (plot in garden.plots.flatMap { line -> line.filter { it.hasGardeners[evenOrOdd] } }) {
                plot.hasGardeners[evenOrOdd] = false
                listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).forEach { (lineDiff, columnDiff) ->
                    val line = plot.line + lineDiff
                    val column = plot.column + columnDiff
                    if (line in garden.plots.indices && column in garden.plots[0].indices) {
                        garden.plots[line][column].hasGardeners[1 - evenOrOdd] = garden.plots[line][column].type.garden
                    }
                }
            }
            evenOrOdd = 1 - evenOrOdd
            step++
        }
        return garden.count()
    }

    fun countGardenPlotsReachedOnInfiniteGardenAfter(steps: Long): Long {
        val center = input.toGarden(0, 0)
        val gardens = mutableMapOf(0 to mutableMapOf(0 to center))
        val initSteps = initGardens(gardens, steps)
        if (initSteps == steps) {
            return gardens.values.sumOf { it.values.sumOf { garden -> garden.count() } }
        }
        var count = 0L
        val bigStep = gardens[3]!![0]!!.init!! - gardens[2]!![0]!!.init!!
        val gardenWidth = 2 + (steps - gardens[2]!![0]!!.init!!) / bigStep
        count += gardenWidth * gardenWidth * center.countAfter(steps)
        count += (gardenWidth - 1) * (gardenWidth - 1) * center.countAfter(steps + 1)
        // line ends
        listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1).forEach { (line, column) ->
            val end = gardens[3 * line]!![3 * column]!!
            val initEnd = (steps - end.init!!).rem(bigStep).toInt()
            count += end.states[initEnd].count
        }
        // triangle ends
        listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1).forEach { (line, column) ->
            val triangle = gardens[line]!![column]!!
            val initEnd = (steps - triangle.init!!).rem(bigStep).toInt()
            count += gardenWidth * triangle.states[initEnd].count
            val initBeforeEnd = ((steps - triangle.init!!).rem(bigStep) + bigStep).toInt()
            count += (gardenWidth - 1) * triangle.states[initBeforeEnd].count
        }
        return count
    }

    private fun initGardens(gardens: MutableMap<Int, MutableMap<Int, Garden>>, steps: Long): Long {
        var evenOrOdd = 0
        var initSteps = 0L
        val lineSize = gardens[0]!![0]!!.plots.size
        val columnSize = gardens[0]!![0]!!.plots[0].size
        val targets = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1, 3 to 0, -3 to 0, 0 to 3, 0 to -3)
        while (initSteps < steps && targets.any { (i, j) -> gardens[i]?.get(j)?.stable == null }) {
            val plots = gardens.values.flatMap { line -> line.values.flatMap { it.plots }.flatten() }
            for (plot in plots.filter { plot -> plot.hasGardeners.any { it } }) {
                plot.hasGardeners[evenOrOdd] = false
                listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).forEach { (lineDiff, columnDiff) ->
                    var line = plot.line + lineDiff
                    var column = plot.column + columnDiff
                    var nextGardenLine = plot.gardenLine
                    var nextGardenColumn = plot.gardenColumn
                    if (line < 0) {
                        nextGardenLine--
                        line = lineSize - 1
                    }
                    if (line == lineSize) {
                        nextGardenLine++
                        line = 0
                    }
                    if (column < 0) {
                        nextGardenColumn--
                        column = columnSize - 1
                    }
                    if (column == columnSize) {
                        nextGardenColumn++
                        column = 0
                    }
                    val nextGarden = gardens.getOrPut(nextGardenLine) { mutableMapOf() }
                        .getOrPut(nextGardenColumn) { input.toEmptyGarden(nextGardenLine, nextGardenColumn) }
                    nextGarden.plots[line][column].hasGardeners[1 - evenOrOdd] =
                        nextGarden.plots[line][column].type.garden
                }
            }
            evenOrOdd = 1 - evenOrOdd
            initSteps++
            gardens.values.flatMap { it.values }.forEach { garden ->
                if (garden.count() != 0L) garden.init = garden.init ?: initSteps
                val state = GardenState(garden.count(), garden.toState())
                if (garden.states.contains(state)) {
                    garden.stable = garden.stable ?: (initSteps - garden.init!! - 1)
                }
                if (garden.init != null && garden.stable == null) {
                    garden.states += state
                }
            }
        }
        return initSteps
    }

    private fun List<String>.toGarden(line: Int, column: Int): Garden {
        return mapIndexed { l, string ->
            string.mapIndexed { c, char ->
                if (char == 'S') {
                    Plot(PlotType.of(char), l, c, line, column, mutableListOf(true, false))
                } else {
                    Plot(PlotType.of(char), l, c, line, column)
                }
            }
        }.let { Garden(line, column, it) }
    }

    private fun List<String>.toEmptyGarden(line: Int, column: Int): Garden {
        return mapIndexed { l, string ->
            string.mapIndexed { c, char -> Plot(PlotType.of(char), l, c, line, column) }
        }.let { Garden(line, column, it) }
    }

    data class Garden(
        val line: Int,
        val column: Int,
        val plots: List<List<Plot>>,
        /**
         * steps before garden is entered
         */
        var init: Long? = null,
        /**
         * steps in garden before garden enters a loop
         */
        var stable: Long? = null,
        /**
         * zeo-indexed list of garden states
         */
        var states: MutableList<GardenState> = mutableListOf(),
    ) {
        fun count(): Long = plots.flatten().count { plot -> plot.hasGardeners.any { it } }.toLong()
        fun countAfter(steps: Long): Long {
            return if (steps >= stable!! - init!!)
                states[(stable!! - init!! + (steps - stable!! - init!!).rem(2)).toInt()].count
            else states[(steps - 1).toInt()].count
        }

        fun toState(): String = plots.joinToString("") { line ->
            line.joinToString("") { plot -> if (plot.hasGardeners.any { it }) "." else " " }
        }

        override fun toString(): String = plots.joinToString("\n") { line ->
            line.joinToString("") { plot -> if (plot.hasGardeners.any { it }) "O" else "${plot.type.char}" }
        }
    }

    data class GardenState(val count: Long, val state: String)
    data class Plot(
        val type: PlotType,
        val line: Int,
        val column: Int,
        val gardenLine: Int,
        val gardenColumn: Int,
        val hasGardeners: MutableList<Boolean> = mutableListOf(false, false)
    )

    enum class PlotType(val char: Char, val garden: Boolean) {
        GARDEN('.', true), ROCK('#', false), GARDENER('S', true);

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }
}

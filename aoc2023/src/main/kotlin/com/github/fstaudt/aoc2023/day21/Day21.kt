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
        val gardenOnEvenSteps = input.toGarden()
        val gardenOnOddSteps = input.toGarden()
        val diffs = listOf(-1, 1)
        for (step in (1..steps)) {
            val garden = if (step.rem(2) == 0) gardenOnOddSteps else gardenOnEvenSteps
            val nextGarden = if (step.rem(2) == 0) gardenOnEvenSteps else gardenOnOddSteps
            nextGarden.flatten().forEach { it.hasGardener = false }
            for (s in garden.flatMap { line -> line.filter { it.hasGardener } }) {
                for (lineDiff in diffs) {
                    if (s.line + lineDiff >= 0 && s.line + lineDiff < garden.size) {
                        nextGarden[s.line + lineDiff][s.column].hasGardener =
                            garden[s.line + lineDiff][s.column].type.garden
                    }
                }
                for (columnDiff in diffs) {
                    if (s.column + columnDiff >= 0 && s.column + columnDiff < garden[0].size) {
                        nextGarden[s.line][s.column + columnDiff].hasGardener =
                            garden[s.line][s.column + columnDiff].type.garden
                    }
                }
            }
//            println(step)
//            println(nextGarden.joinToString("\n") { line -> line.joinToString("") { if (it.hasGardener) "O" else "${it.type.char}" } })
        }
        val garden = if (steps.rem(2) == 0) gardenOnEvenSteps else gardenOnOddSteps
        return garden.flatten().count { it.hasGardener }.toLong()
    }

    fun countGardenPlotsReachedOnInfiniteGardenAfter(steps: Int): Long {
        val gardenOnEvenSteps = input.toGarden()
        val gardenOnOddSteps = input.toGarden()
        val diffs = listOf(-1, 1)
        for (step in (1..steps)) {
            val garden = if (step.rem(2) == 0) gardenOnOddSteps else gardenOnEvenSteps
            val nextGarden = if (step.rem(2) == 0) gardenOnEvenSteps else gardenOnOddSteps
            nextGarden.flatten().forEach { plot -> plot.gardenLevels.forEach { it.value.clear() } }
            for (s in garden.flatMap { line -> line.filter { it.gardenLevels.isNotEmpty() } }) {
                for (gardenLine in s.gardenLevels.keys) {
                    for (gardenColumn in s.gardenLevels[gardenLine]!!.keys) {
                        for (lineDiff in diffs) {
                            var nextLine = s.line + lineDiff
                            var nextGardenLine = gardenLine
                            if (nextLine < 0) {
                                nextGardenLine = gardenLine - 1
                                nextLine = garden.size - 1
                            }
                            if (nextLine >= garden.size) {
                                nextGardenLine = gardenLine + 1
                                nextLine = 0
                            }
                            if (garden[nextLine][s.column].type.garden) {
                                with(nextGarden[nextLine][s.column]) {
                                    val gardenLevel =
                                        gardenLevels[nextGardenLine] ?: mutableMapOf<Int, Boolean>().also {
                                            gardenLevels[nextGardenLine] = it
                                        }
                                    gardenLevel[gardenColumn] = garden[nextLine][s.column].type.garden
                                }
                            }
                        }
                        for (columnDiff in diffs) {
                            var nextColumn = s.column + columnDiff
                            var nextGardenColumn = gardenColumn
                            if (nextColumn < 0) {
                                nextGardenColumn = gardenColumn - 1
                                nextColumn = garden[0].size - 1
                            }
                            if (nextColumn >= garden[0].size) {
                                nextGardenColumn = gardenColumn + 1
                                nextColumn = 0
                            }
                            if (garden[s.line][nextColumn].type.garden) {
                                with(nextGarden[s.line][nextColumn]) {
                                    val gardenLevel = gardenLevels[gardenLine] ?: mutableMapOf<Int, Boolean>().also {
                                        gardenLevels[gardenLine] = it
                                    }
                                    gardenLevel[nextGardenColumn] = true
                                }
                            }
                        }
                    }
                }
            }
//            println(step)
//            println(nextGarden.joinToString("\n") { line -> line.joinToString("") { if (it.hasGardener) "O" else "${it.type.char}" } })
        }
        val garden = if (steps.rem(2) == 0) gardenOnEvenSteps else gardenOnOddSteps
        return garden.flatten().sumOf { plot -> plot.gardenLevels.values.sumOf { it.count { it.value }.toLong() } }
    }

    private fun List<String>.toGarden(): List<List<Plot>> {
        return mapIndexed { l, line ->
            line.mapIndexed { c, char ->
                if (char == 'S') {
                    Plot(PlotType.of(char), l, c, true, mutableMapOf(0 to mutableMapOf(0 to true)))
                } else {
                    Plot(PlotType.of(char), l, c)
                }
            }
        }
    }

    data class Plot(
        val type: PlotType,
        val line: Int,
        val column: Int,
        var hasGardener: Boolean = false,
        val gardenLevels: MutableMap<Int, MutableMap<Int, Boolean>> = mutableMapOf()
    )

    enum class PlotType(val char: Char, val garden: Boolean) {
        GARDEN('.', true), ROCK('#', false), GARDENER('S', true);

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }
}

package com.github.fstaudt.aoc2023.day17

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import kotlin.Long.Companion.MAX_VALUE

fun main() {
    Day17().run()
}

class Day17(fileName: String = "day_17.txt") : Day {
    companion object {
        private const val MAX = MAX_VALUE - 100
    }

    override val input: List<String> = readInputLines(fileName)

    override fun part1() = input.toCity().toMinimalHeatLossFor((1..3))

    override fun part2() = input.toCity().toMinimalHeatLossFor((4..10))

    fun minimalHeatLoss(input: List<String>) = input.toCity().toMinimalHeatLossFor((1..3))

    private fun List<List<Block>>.toMinimalHeatLossFor(range: IntRange): Long {
        val minimalHeatLosses = map { line -> line.map { MinimalHeatLoss() } }
        minimalHeatLosses[0][0].apply {
            fromDown = 0
            fromRight = 0
        }
        var previousState = ""
        var state = minimalHeatLosses.toState()
        while(state != previousState) {
            for (line in indices) {
                for (column in get(0).indices) {
                    minimalHeatLosses[line][column].also { min ->
                        // fromUp
                        range.filter { line - it in indices }.forEach { diff ->
                            val heatLoss: Long = (0..<diff).sumOf { block(line - it, column).heatLoss }
                            minimalHeatLosses[line - diff][column].fromLeft?.let {
                                min.fromUp = (it + heatLoss).coerceAtMost(min.fromUp ?: MAX)
                            }
                            minimalHeatLosses[line - diff][column].fromRight?.let {
                                min.fromUp = (it + heatLoss).coerceAtMost(min.fromUp ?: MAX)
                            }
                        }
                        // fromDown
                        range.filter { line + it in indices }.forEach { diff ->
                            val heatLoss: Long = (0..<diff).sumOf { block(line + it, column).heatLoss }
                            minimalHeatLosses[line + diff][column].fromLeft?.let {
                                min.fromDown = (it + heatLoss).coerceAtMost(min.fromDown ?: MAX)
                            }
                            minimalHeatLosses[line + diff][column].fromRight?.let {
                                min.fromDown = (it + heatLoss).coerceAtMost(min.fromDown ?: MAX)
                            }
                        }
                        // fromLeft
                        range.filter { column - it in get(0).indices }.forEach { diff ->
                            val heatLoss: Long = (0..<diff).sumOf { block(line, column - it).heatLoss }
                            minimalHeatLosses[line][column - diff].fromUp?.let {
                                min.fromLeft = (it + heatLoss).coerceAtMost(min.fromLeft ?: MAX)
                            }
                            minimalHeatLosses[line][column - diff].fromDown?.let {
                                min.fromLeft = (it + heatLoss).coerceAtMost(min.fromLeft ?: MAX)
                            }
                        }
                        // fromRight
                        range.filter { column + it in get(0).indices }.forEach { diff ->
                            val heatLoss: Long = (0..<diff).sumOf { block(line, column + it).heatLoss }
                            minimalHeatLosses[line][column + diff].fromUp?.let {
                                min.fromRight = (it + heatLoss).coerceAtMost(min.fromRight ?: MAX)
                            }
                            minimalHeatLosses[line][column + diff].fromDown?.let {
                                min.fromRight = (it + heatLoss).coerceAtMost(min.fromRight ?: MAX)
                            }
                        }
                    }
                }
            }
            previousState = state
            state = minimalHeatLosses.toState()
        }
        return minimalHeatLosses.last().last().let { (it.fromUp ?: MAX).coerceAtMost(it.fromLeft ?: MAX) }
    }

    private data class Block(val line: Int, val column: Int, val heatLoss: Long)

    private fun List<String>.toCity(): List<List<Block>> {
        return mapIndexed { l, line -> line.mapIndexed { c, it -> Block(l, c, it.digitToInt().toLong()) } }
    }

    private fun List<List<Block>>.block(line: Int, column: Int) = get(line)[column]
    private data class MinimalHeatLoss(
        var fromRight: Long? = null,
        var fromLeft: Long? = null,
        var fromUp: Long? = null,
        var fromDown: Long? = null
    ) {
        override fun toString() = "[$fromUp, $fromDown, $fromLeft, $fromRight]"
    }

    private fun List<List<MinimalHeatLoss>>.toState() = joinToString("\n") { line ->
        line.joinToString(" ") { it.toString() }
    }
}

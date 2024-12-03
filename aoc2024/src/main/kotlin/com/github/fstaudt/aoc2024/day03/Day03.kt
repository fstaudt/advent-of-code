package com.github.fstaudt.aoc2024.day03

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day03().run()
}

class Day03(fileName: String = "day_03.txt") : Day {
    companion object {
        private const val MUL_PATTERN = """mul\((?<first>\d{1,3}),(?<second>\d{1,3})\)"""
        private const val MUL_DO_PATTERN = """$MUL_PATTERN|do\(\)|don't\(\)"""
    }

    override val input: List<String> = readInputLines(fileName)
    override fun part1() = sumMulls()
    override fun part2() = sumMullsWithNewInstructions()

    private fun sumMulls() = input.flatMap { MUL_PATTERN.toRegex().findAll(it) }.sumOf { it.toMull() }

    private fun sumMullsWithNewInstructions(): Long {
        var doMull = true
        return input.flatMap { MUL_DO_PATTERN.toRegex().findAll(it) }
            .sumOf {
                when {
                    it.value == "do()" -> 0L.also { doMull = true }
                    it.value == "don't()" -> 0L.also { doMull = false }
                    doMull -> it.toMull()
                    else -> 0L
                }
            }
    }

    private fun MatchResult.toMull() = groups["first"]!!.value.toLong() * groups["second"]!!.value.toLong()
}
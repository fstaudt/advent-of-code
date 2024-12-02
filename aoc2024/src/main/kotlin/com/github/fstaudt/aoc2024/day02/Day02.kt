package com.github.fstaudt.aoc2024.day02

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitInts
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    Day02().run()
}

class Day02(fileName: String = "day_02.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val reports = input.map { Report(it.splitInts()) }

    override fun part1() = reports.count { it.isSafe() }.toLong()

    override fun part2() = reports.count { it.isSafeWithDampener() }.toLong()

    data class Report(val levels: List<Int>) {

        private val levelPairs = levels.zipWithNext()
        private val direction: Int = levelPairs.sumOf { (it.second - it.first).sign }.sign

        fun isSafe(): Boolean {
            return levelPairs.all { it.isSafeFor(direction) }
        }

        fun isSafeWithDampener(): Boolean {
            if (isSafe()) return true
            val firstProblem = levelPairs.indexOfFirst { !it.isSafeFor(direction) }
            return Report(levels.removeAt(firstProblem)).isSafe() || Report(levels.removeAt(firstProblem + 1)).isSafe()
        }

        private fun Pair<Int, Int>.isSafeFor(direction: Int): Boolean {
            return (second - first).let { it.sign == direction && abs(it) >= 1 && abs(it) <= 3 }
        }

        private fun List<Int>.removeAt(index: Int) = toMutableList().also { it.removeAt(index) }.toList()
    }
}
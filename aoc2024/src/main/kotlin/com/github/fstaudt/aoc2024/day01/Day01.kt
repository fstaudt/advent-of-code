package com.github.fstaudt.aoc2024.day01

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitNotEmpty
import kotlin.math.abs

fun main() {
    Day01().run()
}

class Day01(fileName: String = "day_01.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val firstList = input.map { it.splitNotEmpty()[0].toLong() }
    private val secondList = input.map { it.splitNotEmpty()[1].toLong() }

    override fun part1(): Long {
        val sortedSecondList = secondList.sorted()
        return firstList.sorted().mapIndexed { i, first -> abs(first - sortedSecondList[i]) }.sum()
    }

    override fun part2() = firstList.sumOf { first -> first * secondList.count { first == it } }
}
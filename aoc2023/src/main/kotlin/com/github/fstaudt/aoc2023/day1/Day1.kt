package com.github.fstaudt.aoc2023.day1

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day1().run()
}

class Day1(fileName: String = "day_1.txt") : Day {

    override val input = readInputLines(fileName)

    override fun part1() = input.sumCalibrationValues()

    override fun part2() = input.sumSpelledCalibrationValues()

    private fun List<String>.sumCalibrationValues() = sumOf { "${it.firstDigit()}${it.lastDigit()}".toLong() }
    private fun List<String>.sumSpelledCalibrationValues() = map { it.replaceSpelledDigits() }.sumCalibrationValues()

    private fun String.firstDigit() = first { it.isDigit() }
    private fun String.lastDigit() = last { it.isDigit() }

    private fun String.replaceSpelledDigits(): String {
        if (length < 2) return this
        return when {
            startsWith("one") -> "1"
            startsWith("two") -> "2"
            startsWith("three") -> "3"
            startsWith("four") -> "4"
            startsWith("five") -> "5"
            startsWith("six") -> "6"
            startsWith("seven") -> "7"
            startsWith("eight") -> "8"
            startsWith("nine") -> "9"
            first().isDigit() -> "${first()}"
            else -> ""
        }.let { "${it}${substring(1).replaceSpelledDigits()}" }
    }
}

package com.github.fstaudt.aoc2023.day12

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitInts

fun main() {
    Day12().run()
}

class Day12(fileName: String = "day_12.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val field = input.map { it.toRow() }

    private fun String.toRow() = Row(split(" ")[0], split(" ")[1].splitInts(','))
    override fun part1() = sumCountOfArrangements()

    override fun part2() = sumCountOfArrangementsWhenUnfolded()

    private fun sumCountOfArrangements(): Long {
        return field.sumOf { it.toArrangements() }
    }

    private fun sumCountOfArrangementsWhenUnfolded(): Long {
        return field
            .map { row -> Row((1..5).map { row.springs }.joinToString("?") { it }, (1..5).flatMap { row.numbers }) }
            .map { it.toArrangements() }
            .sumOf { it }
    }

    data class Row(val springs: String, val numbers: List<Int> = emptyList()) {
        companion object {
            private val countCache = mutableMapOf<String, Long>()
            private val countAfterDamagedCache = mutableMapOf<String, Long>()
        }

        fun toArrangements(): Long {
            val masks = ".${numbers.joinToString(".") { "#".repeat(it) }}."
            return count(springs, masks)
        }

        private fun count(springs: String, masks: String): Long {
            val cacheKey = "$masks+$springs"
            return countCache[cacheKey] ?: basicCount(springs, masks) ?: when (springs.first()) {
                '.' ->
                    if (masks.first() == '.') count(springs.drop(1), masks)
                    else 0

                '#' ->
                    if (masks.first() == '.') countAfterDamaged(springs, masks.drop(1))
                    else countAfterDamaged(springs.drop(1), masks.drop(1))

                else -> // '?'
                    if (masks.first() == '.') count(springs.drop(1), masks) + countAfterDamaged(springs, masks.drop(1))
                    else countAfterDamaged(springs.drop(1), masks.drop(1))
            }.also { countCache[cacheKey] = it }
        }

        private fun countAfterDamaged(springs: String, masks: String): Long {
            val cacheKey = "$masks+$springs"
            return countAfterDamagedCache[cacheKey] ?: basicCount(springs, masks) ?: when (springs.first()) {
                '.' ->
                    if (masks.first() == '.') count(springs.drop(1), masks)
                    else 0

                '#' ->
                    if (masks.first() == '.') 0
                    else countAfterDamaged(springs.drop(1), masks.drop(1))

                else -> // '?'
                    if (masks.first() == '.') count(springs.drop(1), masks)
                    else countAfterDamaged(springs.drop(1), masks.drop(1))
            }.also { countAfterDamagedCache[cacheKey] = it }
        }

        private fun basicCount(springs: String, masks: String): Long? {
            return when {
                masks == "." && springs.all { it == '.' || it == '?' } -> 1
                masks == "." -> 0
                springs.isEmpty() -> 0
                else -> null
            }
        }
    }
}

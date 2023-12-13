package com.github.fstaudt.aoc2023.day13

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.flip
import com.github.fstaudt.aoc2023.shared.readInputLines
import kotlin.math.min

fun main() {
    Day13().run()
}

class Day13(fileName: String = "day_13.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val patterns = input.toPatterns()

    override fun part1() = sumReflectionNotes { b, a -> a == b }

    override fun part2() = sumReflectionNotes { b, a -> smudges(b, a) == 1 }

    private fun List<String>.toPatterns(): List<Pattern> {
        if (isEmpty()) return emptyList()
        return Pattern(takeWhile { it.isNotBlank() }).let { pattern ->
            listOf(pattern) + drop(pattern.lines.size).dropWhile { it.isBlank() }.toPatterns()
        }
    }

    private fun sumReflectionNotes(matcher: (before: List<String>, after: List<String>) -> Boolean): Long {
        val rows = patterns.mapNotNull { it.toReflectionRow(matcher) }
        val columns = patterns.map { it.flip() }.mapNotNull { it.toReflectionRow(matcher) }
        return rows.sumOf { it * 100L } + columns.sumOf { it }
    }

    private fun Pattern.toReflectionRow(matcher: (before: List<String>, after: List<String>) -> Boolean): Int? {
        return with(lines) {
            (1..<size).firstNotNullOfOrNull { index ->
                val linesBefore = take(index).takeLast(min(index, size - index))
                val linesAfter = takeLast(size - index).take(min(index, size - index)).reversed()
                index.takeIf { matcher(linesBefore, linesAfter) }
            }
        }
    }

    private fun smudges(before: List<String>, after: List<String>): Int {
        return before.mapIndexed { bi, line ->
            line.mapIndexedNotNull { li, c ->
                c.takeIf { it != after[bi][li] }
            }.count()
        }.sumOf { it }
    }
    data class Pattern(val lines: List<String>) {
        fun flip() = Pattern(lines.flip())
        override fun toString() = "Pattern[\n${lines.joinToString("\n")}\n]"
    }
}

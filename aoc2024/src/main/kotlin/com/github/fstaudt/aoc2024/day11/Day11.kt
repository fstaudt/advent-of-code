package com.github.fstaudt.aoc2024.day11

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitNotEmpty

fun main() {
    Day11().run()
}

class Day11(fileName: String = "day_11.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val numbers = input[0].splitNotEmpty(' ').map { it.toLong() }

    override fun part1() = numbers.map { Stone(it) }.toMutableList().toStonesAfter(25).count().toLong()

    override fun part2() = numbers.sumOf { countStonesAfter(it, 75) }

    private fun MutableList<Stone>.toStonesAfter(blinks: Int): MutableList<Stone> {
        if (blinks == 0) return this
        for (i in (0..<size).reversed()) {
            val number = get(i)
            val value = "${number.number}"
            when {
                value == "0" -> number.number = 1
                value.length % 2 == 0 -> {
                    val half = value.length / 2
                    number.number = value.take(half).toLong()
                    add(i, Stone(value.takeLast(half).toLong()))
                }

                else -> number.number = number.number * 2024
            }
        }
        return toStonesAfter(blinks - 1)
    }

    private val sizes: MutableMap<Int, MutableMap<Long, Long>> = mutableMapOf()
    private fun countStonesAfter(number: Long, blinks: Int): Long {
        if (blinks == 0) return 1L
        sizes[blinks]?.get(number)?.also { return it }
        return when {
            number == 0L -> countStonesAfter(1, blinks - 1)
            "$number".length % 2 == 0 -> {
                val value = "$number"
                val half = value.length / 2
                countStonesAfter(value.take(half).toLong(), blinks - 1) +
                    countStonesAfter(value.takeLast(half).toLong(), blinks - 1)
            }

            else -> countStonesAfter(number * 2024, blinks - 1)
        }.also { sizes.getOrPut(blinks) { mutableMapOf() }.getOrPut(number) { it } }
    }

    data class Stone(var number: Long)
}
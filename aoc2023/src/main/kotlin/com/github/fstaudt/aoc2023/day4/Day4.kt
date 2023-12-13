package com.github.fstaudt.aoc2023.day4

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import com.github.fstaudt.aoc2023.shared.splitLongs
import com.github.fstaudt.aoc2023.shared.toGroupValue
import kotlin.math.min

fun main() {
    Day4().run()
}


class Day4(fileName: String = "day_4.txt") : Day {

    override val input: List<String> = readInputLines(fileName)

    override fun part1() = input.sumOfPoints()

    override fun part2() = input.sumOfAccumulatedScratchCards()

    private fun List<String>.sumOfPoints() = map { it.toCard() }.sumOf { it.toPoints() }
    private fun List<String>.sumOfAccumulatedScratchCards(): Long {
        return map { it.toCard() }.also {
            it.forEachIndexed { index, card ->
                for (i in 1..min(card.numberOfWinningNumbers(), it.size - index - 1)) {
                    it[index + i].instances += card.instances
                }
            }
        }.sumOf { it.instances }
    }

    private fun String.toCard(): Card {
        return split(":").let { card ->
            val id = card[0].toGroupValue("Card +(\\d+)", 1).toLong()
            card[1].split("|").let { allNumbers ->
                val winningNumbers = allNumbers[0].splitLongs()
                val numbers = allNumbers[1].splitLongs()
                Card(id, 1, winningNumbers, numbers)
            }
        }
    }

    data class Card(val id: Long, var instances: Long, val winningNumbers: List<Long>, val numbers: List<Long>) {
        fun toPoints(): Long {
            return numbers.filter { winningNumbers.contains(it) }.takeIf { it.isNotEmpty() }?.let {
                it.fold(1L) { acc, _ -> acc * 2 } / 2
            } ?: 0L
        }

        fun numberOfWinningNumbers() = numbers.count { winningNumbers.contains(it) }
    }
}







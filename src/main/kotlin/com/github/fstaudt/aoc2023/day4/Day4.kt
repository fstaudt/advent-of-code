package com.github.fstaudt.aoc2023.day4

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import kotlin.math.min

fun main() {
    Day4().run()
}


class Day4 : Day {

    override val input: List<String> = readInputLines(4)

    override fun part1() = input.sumOfPoints()

    override fun part2() = input.sumOfAccumulatedScratchCards()

    private fun List<String>.sumOfPoints() = map { it.toCard() }.sumOf { it.toPoints() }
    private fun List<String>.sumOfAccumulatedScratchCards(): Int {
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
            val id = Regex("Card +(\\d+)").find(card[0])!!.groupValues[1].toInt()
            card[1].split("|").let { allNumbers ->
                val winningNumbers = allNumbers[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                val numbers = allNumbers[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                Card(id, 1, winningNumbers, numbers)
            }
        }
    }

    data class Card(val id: Int, var instances: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
        fun toPoints(): Int {
            return numbers.filter { winningNumbers.contains(it) }.takeIf { it.isNotEmpty() }?.let {
                it.fold(1) { acc, _ -> acc * 2 } / 2
            } ?: 0
        }

        fun numberOfWinningNumbers() = numbers.count { winningNumbers.contains(it) }
    }
}







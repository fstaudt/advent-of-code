package com.github.fstaudt.aoc2023.day7

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc2023.day7.Day7.Card.CJ
import com.github.fstaudt.aoc2023.day7.Day7.Type.*

fun main() {
    Day7().run()
}

class Day7(fileName: String = "day_7.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val hands = input.map { it.toHand() }

    override fun part1() = sumWinnings()

    override fun part2() = sumWinningsWithJokers()

    internal fun hand(s: String) = s.toHand()
    private fun sumWinnings(): Long {
        return hands.sortedWith { hand1, hand2 ->
            val type1 = hand1.type()
            val type2 = hand2.type()
            if (type1 == type2) {
                hand1.cards.mapIndexed { index, card -> hand2.cards[index].ordinal - card.ordinal }.first { it != 0 }
            } else {
                type2.ordinal - type1.ordinal
            }
        }.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum().toLong()
    }

    private fun sumWinningsWithJokers(): Long {
        return hands.sortedWith { hand1, hand2 ->
            val type1 = hand1.typeWithJokers()
            val type2 = hand2.typeWithJokers()
            if (type1 == type2) {
                hand1.cards.mapIndexed { index, card -> hand2.cards[index].valueWithJoker - card.valueWithJoker }
                    .first { it != 0 }
            } else {
                type2.ordinal - type1.ordinal
            }
        }.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum().toLong()
    }

    private fun String.toHand(): Hand {
        val cards = split(' ')[0].map { Card.valueOf("C$it") }
        return Hand(cards, split(' ')[1].toInt())
    }

    enum class Card(val valueWithJoker: Int) {
        CA(1),
        CK(2),
        CQ(3),
        CJ(13),
        CT(4),
        C9(5),
        C8(6),
        C7(7),
        C6(8),
        C5(9),
        C4(10),
        C3(11),
        C2(12)
    }

    enum class Type {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIRS,
        ONE_PAIR,
        HIGH_CARD
    }

    data class Hand(val cards: List<Card>, val bid: Int) {
        fun type(): Type {
            return when {
                cards.all { it == cards[0] } -> FIVE_OF_A_KIND
                cards.groupBy { it }.values.maxOf { it.size } == 4 -> FOUR_OF_A_KIND
                cards.groupBy { it }.values.let { it.maxOf { it.size } == 3 && it.size == 2 } -> FULL_HOUSE
                cards.groupBy { it }.values.maxOf { it.size } == 3 -> THREE_OF_A_KIND
                cards.groupBy { it }.values.let { it.count { it.size == 2 } == 2 } -> TWO_PAIRS
                cards.groupBy { it }.values.let { it.count { it.size == 2 } == 1 } -> ONE_PAIR
                else -> HIGH_CARD
            }
        }

        fun typeWithJokers(): Type {
            val jokers = cards.count { it == CJ }
            return when {
                jokers == 5 -> FIVE_OF_A_KIND
                cards.filter { it != CJ }.groupBy { it }.values.size == 1 -> FIVE_OF_A_KIND
                cards.filter { it != CJ }.groupBy { it }.values.maxOf { it.size + jokers } == 4 -> FOUR_OF_A_KIND
                cards.filter { it != CJ }.groupBy { it }.values.size == 2 -> FULL_HOUSE
                cards.filter { it != CJ }.groupBy { it }.values.maxOf { it.size + jokers } == 3 -> THREE_OF_A_KIND
                cards.filter { it != CJ }.groupBy { it }.values.size == 3 -> TWO_PAIRS
                cards.filter { it != CJ }.groupBy { it }.values.size == 4 -> ONE_PAIR
                else -> HIGH_CARD
            }
        }
    }
}

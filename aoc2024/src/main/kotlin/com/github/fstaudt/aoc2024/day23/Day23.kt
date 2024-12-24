package com.github.fstaudt.aoc2024.day23

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day23().run()
}

class Day23(fileName: String = "day_23.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val connections = input.map { it.split("-").sorted() }.sortedBy { it.joinToString() }
    private val connectionsMap = connections.groupBy { it[0] }.mapValues { cn -> cn.value.map { it[1] }.toSet() }

    override fun part1(): Long {
        val lanParties = connections.map { LanParty(it) }.toBiggerLanParties()
        return lanParties.filter { cluster -> cluster.list.any { it.startsWith("t") } }.size.toLong()
    }

    override fun part2() = 0L.also { println("Part 2: ${part2AsString()}") }
    fun part2AsString(): String {
        var bigLanParties = connections.map { LanParty(it) }
        while (bigLanParties.size > 1) {
            bigLanParties = bigLanParties.toBiggerLanParties()
        }
        return bigLanParties.first().list.joinToString(",")
    }

    data class LanParty(val list: List<String>) {
        val withoutLast = list.dropLast(1).joinToString("-")
        val last = list.last()
    }

    private fun List<LanParty>.toBiggerLanParties(): List<LanParty> {
        val biggerLanParties = mutableListOf<LanParty>()
        forEachIndexed { i, lanParty ->
            drop(i + 1).takeWhile { it.withoutLast == lanParty.withoutLast }.forEach { otherParty ->
                if (connectionsMap[lanParty.last]?.contains(otherParty.last) == true) {
                    biggerLanParties.add(LanParty(lanParty.list + otherParty.last))
                }
            }
        }
        return biggerLanParties
    }
}
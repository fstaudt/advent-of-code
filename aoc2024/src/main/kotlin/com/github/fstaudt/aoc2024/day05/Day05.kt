package com.github.fstaudt.aoc2024.day05

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import java.lang.Integer.MAX_VALUE

fun main() {
    Day05().run()
}

class Day05(fileName: String = "day_05.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    private val rules = input.takeWhile { it.isNotBlank() }
        .map { it.split("|").let { (before, after) -> Rule(before.toInt(), after.toInt()) } }
    private val updates = input.takeLastWhile { it.isNotBlank() }.map { Update(it.split(",").map { it.toInt() }) }

    override fun part1() = updates.filter { it.satisfy(rules) }.sumOf { it.middle() }
    override fun part2() = updates.filterNot { it.satisfy(rules) }.sumOf { it.toFixedUpdateFor(rules).middle() }

    data class Rule(val before: Int, val after: Int)

    data class Update(val numbers: List<Int>) {
        fun middle() = numbers[numbers.size / 2].toLong()

        fun satisfy(rules: List<Rule>) = rules.all { satisfy(it) }
        fun satisfy(rule: Rule): Boolean {
            val before = numbers.indexOf(rule.before)
            val after = numbers.lastIndexOf(rule.after).takeIf { it >= 0 } ?: MAX_VALUE
            return before < after
        }

        fun toFixedUpdateFor(rules: List<Rule>): Update {
            val orderedNumbers = numbers
                .map { number -> number to rules.count { it.before == number && numbers.contains(it.after) } }
                .sortedByDescending { it.second }.map { it.first }
            return Update(orderedNumbers)
        }
    }
}
package com.github.fstaudt.aoc2024.day22

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day22().run()
}

class Day22(fileName: String = "day_22.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        var secrets = input.map { it.toLong() }
        (1..2000).forEach { _ ->
            secrets = secrets
                .map { (it * 64).mix(it).prune() }
                .map { (it / 32).mix(it).prune() }
                .map { (it * 2048).mix(it).prune() }
        }
        return secrets.sumOf { it }
    }

    override fun part2(): Long {
        var secrets = input.map { mutableListOf(Secret(it.toLong(), it.toLong() % 10, 0)) }
        (1..2000).forEach { _ ->
            secrets = secrets
                .map { list ->
                    val last = list.last()
                    val nextValue = (last.value * 64).mix(last.value).prune()
                        .let { (it / 32).mix(it).prune() }
                        .let { (it * 2048).mix(it).prune() }
                    list.also { it.add(Secret(nextValue, nextValue % 10, (nextValue % 10) - last.price)) }
                }
        }
        val sequences = secrets.map { list ->
            list.asSequence().drop(1).windowed(4)
                .map { window -> Sequence(window.map { it.diff }, window.last().price) }
                .distinctBy { it.diffs }.associate { it.diffs to it.price }
        }
        return sequences.flatMap { seq -> seq.map { it.key }.filter { it.last() > 0 && it.sum() > 0 } }.distinct()
            .maxOf { diffs -> sequences.sumOf { it[diffs] ?: 0 } }
    }

    private fun Long.mix(secret: Long) = xor(secret)
    private fun Long.prune() = this % 16777216

    data class Secret(val value: Long, val price: Long, val diff: Long)
    data class Sequence(val diffs: List<Long>, val price: Long)
}
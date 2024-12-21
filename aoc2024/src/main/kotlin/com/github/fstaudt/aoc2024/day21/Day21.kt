package com.github.fstaudt.aoc2024.day21

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day21().run()
}

class Day21(fileName: String = "day_21.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        return input.sumOf { code ->
            code.toNumericalCode().toDirectionalCode().toDirectionalCode().length * code.dropLast(1).toLong()
        }
    }

    override fun part2(): Long {
        var keypads = 0
        var counts = DirectionalPadCount(directionalPad.buttons.mapValues { button ->
            ButtonCount(button.value.paths.mapValues { Count(it.value) })
        })
        while (++keypads < 25) {
            val nextCounts = mutableMapOf<Char, ButtonCount>()
            counts.buttons.forEach { (from, button) ->
                val nextPaths = mutableMapOf<Char, Count>()
                nextCounts[from] = ButtonCount(nextPaths)
                button.paths.forEach { (to, path) ->
                    val nextCount = path.path.toCharPairs().sumOf { (i, j) -> counts.buttons[i]!!.paths[j]!!.count }
                    nextPaths[to] = Count(path.path, nextCount)
                }
            }
            counts = DirectionalPadCount(nextCounts)
        }
        return input.sumOf { code ->
            code.toNumericalCode().toCharPairs().sumOf { (i, j) -> counts.buttons[i]!!.paths[j]!!.count } *
                code.dropLast(1).toLong()
        }
    }

    private fun String.toNumericalCode(): String {
        return toCharPairs().mapNotNull { (i, j) -> numericalPad.buttons[i]!!.paths[j] }.joinToString("")
    }

    private fun String.toDirectionalCode(): String {
        return toCharPairs().mapNotNull { (i, j) -> directionalPad.buttons[i]!!.paths[j] }.joinToString("")
    }

    private fun String.toCharPairs() = "A$this".map { it }.windowed(2)

    private val directionalPad = DirectionalPad(
        mapOf(
            'A' to Button(mapOf('A' to "A", '^' to "<A", '>' to "vA", 'v' to "<vA", '<' to "v<<A")),
            '^' to Button(mapOf('A' to ">A", '^' to "A", '>' to "v>A", 'v' to "vA", '<' to "v<A")),
            '>' to Button(mapOf('A' to "^A", '^' to "<^A", '>' to "A", 'v' to "<A", '<' to "<<A")),
            'v' to Button(mapOf('A' to "^>A", '^' to "^A", '>' to ">A", 'v' to "A", '<' to "<A")),
            '<' to Button(mapOf('A' to ">>^A", '^' to ">^A", '>' to ">>A", 'v' to ">A", '<' to "A")),
        )
    )
    private val numericalPad = NumericalPad(
        mapOf(
            '0' to Button(mapOf('0' to "A", 'A' to ">A", '1' to "^<A", '2' to "^A", '3' to "^>A", '4' to "^^<A", '5' to "^^A", '6' to "^^>A", '7' to "^^^<A", '8' to "^^^A", '9' to "^^^>A")),
            'A' to Button(mapOf('0' to "<A", 'A' to "A", '1' to "^<<A", '2' to "<^A", '3' to "^A", '4' to "^^<<A", '5' to "<^^A", '6' to "^^A", '7' to "^^^<<A", '8' to "<^^^A", '9' to "^^^A")),
            '1' to Button(mapOf('0' to ">vA", 'A' to ">>vA", '1' to "A", '2' to ">A", '3' to ">>A", '4' to "^A", '5' to "^>A", '6' to "^>>A", '7' to "^^A", '8' to "^^>A", '9' to "^^>>A")),
            '2' to Button(mapOf('0' to "vA", 'A' to "v>A", '1' to "<A", '2' to "A", '3' to ">A", '4' to "<^A", '5' to "^A", '6' to "^>A", '7' to "<^^A", '8' to "^^A", '9' to "^^>A")),
            '3' to Button(mapOf('0' to "<vA", 'A' to "vA", '1' to "<<A", '2' to "<A", '3' to "A", '4' to "<<^A", '5' to "<^A", '6' to "^A", '7' to "<<^^A", '8' to "<^^A", '9' to "^^A")),
            '4' to Button(mapOf('0' to ">vvA", 'A' to ">>vvA", '1' to "vA", '2' to "v>A", '3' to "v>>A", '4' to "A", '5' to ">A", '6' to ">>A", '7' to "^A", '8' to "^>A", '9' to "^>>A")),
            '5' to Button(mapOf('0' to "vvA", 'A' to "vv>A", '1' to "<vA", '2' to "vA", '3' to "v>A", '4' to "<A", '5' to "A", '6' to ">A", '7' to "<^A", '8' to "^A", '9' to "^>A")),
            '6' to Button(mapOf('0' to "<vvA", 'A' to "vvA", '1' to "<<vA", '2' to "<vA", '3' to "vA", '4' to "<<A", '5' to "<A", '6' to "A", '7' to "<<^A", '8' to "<^A", '9' to "^A")),
            '7' to Button(mapOf('0' to ">vvvA", 'A' to ">>vvvA", '1' to "vvA", '2' to "vv>A", '3' to "vv>>A", '4' to "vA", '5' to "v>A", '6' to "v>>A", '7' to "A", '8' to ">A", '9' to ">>A")),
            '8' to Button(mapOf('0' to "vvvA", 'A' to "vvv>A", '1' to "<vvA", '2' to "vvA", '3' to "vv>A", '4' to "<vA", '5' to "vA", '6' to "v>A", '7' to "<A", '8' to "A", '9' to ">A")),
            '9' to Button(mapOf('0' to "<vvvA", 'A' to "vvvA", '1' to "<<vvA", '2' to "<vvA", '3' to "vvA", '4' to "<<vA", '5' to "<vA", '6' to "vA", '7' to "<<A", '8' to "<A", '9' to "A")),
        )
    )

    private data class NumericalPad(val buttons: Map<Char, Button>)
    private data class DirectionalPad(val buttons: Map<Char, Button>)
    private data class Button(val paths: Map<Char, String>)
    private data class DirectionalPadCount(val buttons: Map<Char, ButtonCount>)
    private data class ButtonCount(val paths: Map<Char, Count>)
    private data class Count(val path: String, val count: Long = path.length.toLong())
}
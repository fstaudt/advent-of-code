package com.github.fstaudt.aoc2023.day10

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc2023.day10.Day10.Pipe.*
import com.github.fstaudt.aoc2023.day10.Day10.Tile

fun main() {
    Day10().run()
}

class Day10(fileName: String = "day_10.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val field = input.toField()
    override fun part1() = numberOfSteps()
    override fun part2() = numberOfEnclosedTiles()

    private fun List<String>.toField(): Field {
        return mapIndexed { line, s ->
            line to s.mapIndexed { column, pipe -> column to Tile(Pipe.byLetter(pipe), line, column) }
                .associate { it.first to it.second }
        }.associate { it.first to it.second }
    }

    private fun numberOfSteps(): Long = field.toLoop().size.toLong() / 2

    private fun Field.toLoop(): List<Tile> {
        val start = values.flatMap { it.values }.first { it.pipe == START }
        val loop = mutableListOf(start)
        var next: Tile = start.nextIn(this, null)
        while (next != start) {
            loop.add(next)
            next = next.nextIn(this, loop[loop.size - 2])
        }
        return loop
    }

    private fun numberOfEnclosedTiles(): Long {
        val loop = field.toLoop()
        loop.forEach {
            field[it.line]!![it.column]!!.border = true
        }
        loop.first().pipe = loop.inferStartPipe()
        val borders = field.borders()
        field.values.flatMap { it.values }.forEach { position ->
            val bordersCrossed = borders[position.line].take(position.column)
                .replace("L-*7".toRegex(), "|")
                .replace("F-*J".toRegex(), "|")
                .count { it == '|' }
            position.enclosed = !position.border && (2 * (bordersCrossed / 2) != bordersCrossed)
        }
        return field.values.flatMap { it.values }.count { it.enclosed }.toLong()
    }

    private fun List<Tile>.inferStartPipe(): Pipe {
        val start = first()
        val line = if (start.line == get(1).line) get(size - 1).line else get(1).line
        val column = if (start.column == get(1).column) get(size - 1).column else get(1).column
        return when {
            line < start.line ->
                when {
                    column < start.column -> NW
                    column > start.column -> NE
                    else -> NS
                }

            line > start.line ->
                when {
                    column < start.column -> SW
                    column > start.column -> SE
                    else -> NS
                }

            else -> WE
        }
    }

    private fun Field.borders(): List<String> {
        return values.map {
            it.values.joinToString("") { position -> if (position.border) "${position.pipe.letter}" else " " }
        }
    }

    enum class Pipe(val letter: Char, val north: Boolean, val east: Boolean, val south: Boolean, val west: Boolean) {
        NS('|', true, false, true, false),
        WE('-', false, true, false, true),
        NE('L', true, true, false, false),
        NW('J', true, false, false, true),
        SW('7', false, false, true, true),
        SE('F', false, true, true, false),
        GROUND('.', false, false, false, false),
        START('S', true, true, true, true);

        companion object {
            fun byLetter(letter: Char): Pipe = entries.first { it.letter == letter }
        }

        override fun toString() = "$letter"
    }

    data class Tile(
        var pipe: Pipe,
        val line: Int,
        val column: Int,
        var border: Boolean = false,
        var enclosed: Boolean = false
    ) {
        fun nextIn(field: Field, last: Tile?): Tile {
            val width = field[0]!!.size
            val height = field.size
            val next = when {
                pipe.north && line > 0 && field[line - 1]!![column]!!.let { it != last && it.pipe.south } -> field[line - 1]!![column]!!
                pipe.east && column < width && field[line]!![column + 1]!!.let { it != last && it.pipe.west } -> field[line]!![column + 1]!!
                pipe.south && line < height && field[line + 1]!![column]!!.let { it != last && it.pipe.north } -> field[line + 1]!![column]!!
                pipe.west && column > 0 && field[line]!![column - 1]!!.let { it != last && it.pipe.east } -> field[line]!![column - 1]!!
                else -> this
            }
            return next
        }
    }
}
typealias Field = Map<Int, Map<Int, Tile>>

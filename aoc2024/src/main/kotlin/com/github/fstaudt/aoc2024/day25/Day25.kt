package com.github.fstaudt.aoc2024.day25

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.flip
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf

fun main() {
    Day25().run()
}

class Day25(fileName: String = "day_25.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val matrices = input.chunked(8).map { chunk -> chunk.take(7).toMatrixOf { it.char } }
    private val locks = matrices.filter { matrix -> matrix.first().all { it == '#' } }
    private val keys = matrices.filter { matrix -> matrix.last().all { it == '#' } }
    private fun Matrix<Char>.heights() = flip().map { pin -> pin.count { it == '#' } - 1 }

    override fun part1(): Long {
        return locks.sumOf { lock ->
            keys.count { key ->
                val lockHeights = lock.heights()
                key.heights().mapIndexed { i, keyHeight -> keyHeight + lockHeights[i] }.all { it <= 5 }
            }
        }.toLong()
    }

    override fun part2() = 0L
}
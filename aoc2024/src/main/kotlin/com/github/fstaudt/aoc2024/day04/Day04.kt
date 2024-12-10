package com.github.fstaudt.aoc2024.day04

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.forEachEntry
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf

fun main() {
    Day04().run()
}

class Day04(fileName: String = "day_04.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val letters: Matrix<Letter> = input.toMatrixOf { Letter(it.i, it.j, it.char) }

    override fun part1(): Long {
        var count = 0L
        letters.forEachEntry { letter ->
            if (letter.char == 'X') {
                if (letters.spellMASFrom(letter, -1, -1)) count++
                if (letters.spellMASFrom(letter, -1, 0)) count++
                if (letters.spellMASFrom(letter, -1, 1)) count++
                if (letters.spellMASFrom(letter, 0, -1)) count++
                if (letters.spellMASFrom(letter, 0, 1)) count++
                if (letters.spellMASFrom(letter, 1, -1)) count++
                if (letters.spellMASFrom(letter, 1, 0)) count++
                if (letters.spellMASFrom(letter, 1, 1)) count++
            }
        }
        return count
    }

    override fun part2(): Long {
        var count = 0L
        letters.forEachEntry { letter ->
            if (letter.char == 'A') {
                val (i, j) = letter
                if ((letters.letter(i - 1, j - 1) == 'M' && letters.letter(i + 1, j + 1) == 'S') ||
                    (letters.letter(i - 1, j - 1) == 'S' && letters.letter(i + 1, j + 1) == 'M')
                ) {
                    if ((letters.letter(i + 1, j - 1) == 'M' && letters.letter(i - 1, j + 1) == 'S') ||
                        (letters.letter(i + 1, j - 1) == 'S' && letters.letter(i - 1, j + 1) == 'M')
                    ) {
                        count++
                    }
                }
            }
        }
        return count
    }

    private fun Matrix<Letter>.spellMASFrom(letter: Letter, deltaI: Int, deltaJ: Int): Boolean {
        return (letter(letter.i + 1 * deltaI, letter.j + 1 * deltaJ) == 'M')
            && (letter(letter.i + 2 * deltaI, letter.j + 2 * deltaJ) == 'A')
            && (letter(letter.i + 3 * deltaI, letter.j + 3 * deltaJ) == 'S')
    }

    private fun Matrix<Letter>.letter(i: Int, j: Int) = getOrNull(i)?.getOrNull(j)?.char ?: ' '
    private data class Letter(val i: Int, val j: Int, val char: Char)
}
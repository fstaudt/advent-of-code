package com.github.fstaudt.aoc2024.day04

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.MatrixExtensions.forEachEntry

fun main() {
    Day04().run()
}

class Day04(fileName: String = "day_04.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val letters = input.mapIndexed { i, line -> line.mapIndexed { j, char -> Letter(char, i, j) } }

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
                val (_, i, j) = letter
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

    private fun List<List<Letter>>.spellMASFrom(letter: Letter, deltaI: Int, deltaJ: Int): Boolean {
        return (letter(letter.i + 1 * deltaI, letter.j + 1 * deltaJ) == 'M')
            && (letter(letter.i + 2 * deltaI, letter.j + 2 * deltaJ) == 'A')
            && (letter(letter.i + 3 * deltaI, letter.j + 3 * deltaJ) == 'S')
    }

    private fun List<List<Letter>>.letter(i: Int, j: Int): Char {
        return if (i >= 0 && i < size && j >= 0 && j < get(0).size) get(i)[j].char else ' '
    }

    private data class Letter(val char: Char, val i: Int, val j: Int)
}
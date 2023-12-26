package com.github.fstaudt.aoc2023.day3

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day3().run()
}

class Day3(fileName: String = "day_3.txt") : Day {

    override val input: List<String> = readInputLines(fileName)

    override fun part1() = input.partNumbers().sumOf { it.value }
    override fun part2() = input.sumGearRatios()


    private fun List<String>.sumGearRatios(): Long {
        val partNumbers = partNumbers().filter { partNumber -> partNumber.symbols.any { it.char == '*' } }
        return partNumbers.mapNotNull { partNumber ->
            partNumbers.find { other ->
                other != partNumber && other.symbols.filter { it.char == '*' }.any { partNumber.symbols.contains(it) }
            }?.let {
                partNumber.value * it.value
            }
        }.sumOf { it } / 2
    }

    private fun List<String>.partNumbers(): List<PartNumber> {
        return (emptyLine() + this + emptyLine()).windowed(3).flatMapIndexed { lineIndex, lines ->
            val line = lines[1]
            val previousLine = lines[0]
            val nextLine = lines[2]
            var index = 0
            lines[1].split(Regex("\\D+")).filter { it.contains(Regex("\\d+")) }.map { value ->
                val partIndex = line.indexOf(value, index)
                val startIndex = max(partIndex - 1, 0)
                val endIndex = min(partIndex + value.length, line.length - 1)
                index = endIndex
                PartNumber(
                    value.toLong(),
                    lineIndex,
                    partIndex,
                    previousLine.substring(startIndex, endIndex + 1).toSymbols(lineIndex, startIndex)
                            + listOf(Symbol(line[startIndex], lineIndex, startIndex)).filter { it.isSymbol() }
                            + listOf(Symbol(line[endIndex], lineIndex, endIndex)).filter { it.isSymbol() }
                            + nextLine.substring(startIndex, endIndex + 1).toSymbols(lineIndex + 2, startIndex)
                )
            }.filter { it.symbols.isNotEmpty() }
        }
    }

    private fun List<String>.emptyLine(): List<String> = listOf(get(0).replace(Regex("."), "."))

    private fun String.toSymbols(lineIndex: Int, startIndex: Int): List<Symbol> {
        return mapIndexed { index, char -> Symbol(char, lineIndex - 1, startIndex + index) }.filter { it.isSymbol() }
    }


    private data class PartNumber(val value: Long, val line: Int, val index: Int, val symbols: List<Symbol>)
    private data class Symbol(val char: Char, val line: Int, val index: Int) {
        fun isSymbol() = char != '.' && !char.isDigit()
    }
}








package com.github.fstaudt.aoc2024.day19

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day19().run()
}

class Day19(fileName: String = "day_19.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val designs = input[0].split(", ")
    private val minDesignLength = designs.minOf { it.length }
    private val patterns = input.drop(1).filter { it.isNotBlank() }
    private val isPossibles = mutableMapOf<String, Boolean>()
    private val countPossibles = mutableMapOf<String, Long>()

    override fun part1() = patterns.count { it.isPossibleWith(designs) }.toLong()

    override fun part2() = patterns.sumOf { it.countPossibleWith(designs) }

    private fun String.isPossibleWith(designs: List<String>): Boolean {
        return isPossibles.getOrPut(this) {
            length < minDesignLength || designs.any { startsWith(it) && drop(it.length).isPossibleWith(designs) }
        }
    }

    private fun String.countPossibleWith(designs: List<String>): Long {
        return countPossibles.getOrPut(this) {
            if (isBlank()) 1L
            else designs.filter { startsWith(it) }.sumOf { drop(it.length).countPossibleWith(designs) }
        }
    }
}


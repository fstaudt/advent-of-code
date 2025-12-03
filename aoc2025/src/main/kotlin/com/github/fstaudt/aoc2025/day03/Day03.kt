package com.github.fstaudt.aoc2025.day03

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
  Day03().run()
}

class Day03(fileName: String = "day_03.txt") : Day {
  override val input: List<String> = readInputLines(fileName)

  override fun part1(): Long {
    return input.sumOf { bank ->
      val first = bank.dropLast(1).toCharArray().max()
      val second = bank.dropWhile { it != first }.drop(1).max()
      "$first$second".toLong()
    }
  }

  override fun part2(): Long {
    return input.sumOf { bank ->
      bank.joltage(12).toLong().also { println("$bank: $it")}
    }
  }

  private fun String.joltage(number: Int): String {
    if (number == 1) return toCharArray().max().toString()
    val first = dropLast(number-1).toCharArray().max()
    val rest = dropWhile{ it != first }.drop(1).joltage(number-1)
    return "$first$rest"
  }
}

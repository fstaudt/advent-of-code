package com.github.fstaudt.aoc2025.day06

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.flip
import com.github.fstaudt.aoc.shared.StringExtensions.splitLongs
import com.github.fstaudt.aoc.shared.StringExtensions.splitNotEmpty

fun main() {
  Day06().run()
}

class Day06(fileName: String = "day_06.txt") : Day {
  override val input: List<String> = readInputLines(fileName)
  val operators: List<String> = input.last().splitNotEmpty()

  override fun part1(): Long = input.dropLast(1).map { it.splitLongs() }.flip().toTotal()

  override fun part2(): Long {
    return input.dropLast(1)
      .map { it.toCharArray().toList() }.flip(" ")
      .map { it.fold("") { s, c -> "$s$c" }.trim() }
      .fold(mutableListOf(mutableListOf<Long>())) { l, s ->
        l.also {
          if (s.isEmpty()) l.add(mutableListOf())
          else l.last().add(s.toLong())
        }
      }.toTotal()
  }

  private fun Matrix<Long>.toTotal(): Long {
    var sum = 0L
    operators.forEachIndexed { i, operator ->
      sum += if (operator == "+")
        get(i).sum()
      else
        get(i).reduce { a, b -> a * b }
    }
    return sum
  }
}
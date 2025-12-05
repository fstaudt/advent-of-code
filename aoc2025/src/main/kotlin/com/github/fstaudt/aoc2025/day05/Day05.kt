package com.github.fstaudt.aoc2025.day05

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
  Day05().run()
}

class Day05(fileName: String = "day_05.txt") : Day {
  override val input: List<String> = readInputLines(fileName)
  private val freshRanges = input.takeWhile { it.isNotBlank() }.map { FreshRange(it) }
  private val ingredients = input.dropWhile { it.isNotBlank() }.drop(1).map { it.toLong() }

  override fun part1() = ingredients.count { ing -> freshRanges.any { ing in it.from..it.until } }.toLong()

  override fun part2(): Long {
    val ranges = freshRanges.sortedBy { it.until }.sortedBy { it.from }
    var freshCount = ranges[0].let { it.until - it.from + 1 }
    ranges.drop(1).forEachIndexed { i, freshRange ->
      val from = if (freshRange.from <= ranges[i].until) ranges[i].until + 1 else freshRange.from
      if (freshRange.until >= from) freshCount += freshRange.until - from + 1
    }
    return freshCount
  }

  private data class FreshRange(val input: String) {
    val from = input.split("-").let { it[0] }.toLong()
    val until = input.split("-").let { it[1] }.toLong()
  }
}
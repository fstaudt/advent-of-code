package com.github.fstaudt.aoc2025.day04

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.countEntries
import com.github.fstaudt.aoc.shared.MatrixExtensions.entry
import com.github.fstaudt.aoc.shared.MatrixExtensions.forEachEntry

fun main() {
  Day04().run()
}

class Day04(fileName: String = "day_04.txt") : Day {
  companion object {
    val adjacentPositions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
  }

  override val input: List<String> = readInputLines(fileName)

  override fun part1(): Long {
    val grid: Matrix<Tile> = input.mapIndexed { i, it -> it.mapIndexed { j, c -> Tile(i, j, c) } }
    return grid.countEntries { it.isPaperRoll() && it.adjacentPaperRollsCountIn(grid) < 4 }.toLong()
  }

  override fun part2(): Long {
    val grid: Matrix<Tile> = input.mapIndexed { i, it -> it.mapIndexed { j, c -> Tile(i, j, c) } }
    val initialPaperRollsCount = grid.countEntries { it.isPaperRoll() }.toLong()
    var finalPaperRollsCount = initialPaperRollsCount
    while (true) {
      grid.forEachEntry {
        if (it.isPaperRoll() && it.adjacentPaperRollsCountIn(grid) < 4) it.char = 'x'
      }
      val intermediatePaperRollsCount = grid.countEntries { it.isPaperRoll() }.toLong()
      if (finalPaperRollsCount == intermediatePaperRollsCount) break
      finalPaperRollsCount = intermediatePaperRollsCount
    }
    return initialPaperRollsCount - finalPaperRollsCount
  }

  private data class Tile(val i: Int, val j: Int, var char: Char) {
    fun isPaperRoll() = char == '@'
    fun adjacentPaperRollsCountIn(grid: Matrix<Tile>): Int {
      return adjacentPositions.count { grid.entry(i + it.first, j + it.second)?.isPaperRoll() ?: false }
    }
  }
}

package com.github.fstaudt.aoc2025.day07

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.countEntries
import com.github.fstaudt.aoc.shared.MatrixExtensions.entry
import com.github.fstaudt.aoc.shared.MatrixExtensions.forEachEntry
import com.github.fstaudt.aoc2025.day07.Day07.TileType.BEAM
import com.github.fstaudt.aoc2025.day07.Day07.TileType.SPACE
import com.github.fstaudt.aoc2025.day07.Day07.TileType.SPLITTER
import com.github.fstaudt.aoc2025.day07.Day07.TileType.START

fun main() {
  Day07().run()
}

class Day07(fileName: String = "day_07.txt") : Day {
  override val input: List<String> = readInputLines(fileName)
  private val manifold: Matrix<Tile> =
    input.mapIndexed { i, l -> l.toCharArray().mapIndexed { j, c -> Tile(TileType.of(c), i, j) } }

  override fun part1(): Long {
    splitBeam()
    return manifold.countEntries {
      it.type == SPLITTER && manifold.entry(it.i - 1, it.j)?.type == BEAM
    }.toLong()
  }

  override fun part2(): Long {
    splitBeam()
    val start = manifold.first().first { it.type == START }
    return timelinesFrom(start)
  }

  private fun splitBeam() {
    manifold.forEachEntry { tile ->
      if (tile.type == SPLITTER) {
        manifold.entry(tile.i, tile.j - 1)?.let { it.type = BEAM }
        manifold.entry(tile.i, tile.j + 1)?.let { it.type = BEAM }
      }
      if (tile.type == SPACE) {
        val upTile = manifold.entry(tile.i - 1, tile.j)
        if (upTile?.type == START || upTile?.type == BEAM) {
          tile.type = BEAM
        }
      }
    }
  }

  private fun timelinesFrom(tile: Tile?): Long {
    if (tile == null) return 1L
    return tile.timelines ?: tile.run {
      when (type) {
        SPLITTER -> timelinesFrom(manifold.entry(i, j - 1)) + timelinesFrom(manifold.entry(i, j + 1))
        else -> timelinesFrom(manifold.entry(i+1, j))
      }.also { timelines = it }
    }
  }

  private enum class TileType(val char: Char) {
    START('S'), SPLITTER('^'), SPACE('.'), BEAM('|');

    companion object {
      fun of(char: Char): TileType = entries.firstOrNull { it.char == char } ?: SPACE
    }
  }

  private data class Tile(var type: TileType, val i: Int, val j: Int, var timelines: Long? = null)
}

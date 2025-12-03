package com.github.fstaudt.aoc2025.day01

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Direction
import com.github.fstaudt.aoc.shared.Direction.LEFT
import com.github.fstaudt.aoc.shared.Direction.RIGHT
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
  Day01().run()
}

class Day01(fileName: String = "day_01.txt") : Day {
  override val input: List<String> = readInputLines(fileName)
  private val rotations = input.map { Rotation(if (it[0] == 'L') LEFT else RIGHT, it.substring(1).toInt()) }

  override fun part1(): Long {
    var password = 0L
    val dial = Dial()
    rotations.forEach {
      dial.point += (it.direction.dj * it.distance)
      if (dial.point < 0) dial.point += 100
      dial.point %= 100
      if (dial.point == 0) password++
    }
    return password
  }

  override fun part2(): Long {
    var password = 0L
    val dial = Dial()
    rotations.forEach {
      val rounds = it.distance / 100
      password += rounds
      val remainder = it.distance % 100
      val oldPoint = dial.point
      dial.point += (it.direction.dj * remainder)
      if (dial.point * oldPoint < 0) {
        password++
      }
      if (dial.point < 0) {
        dial.point += 100
      }
      if (dial.point > 100) {
        password++
      }
      dial.point %= 100
      if (dial.point == 0) password++
      println("Rotation of ${it.direction.dj * it.distance}: ${dial}, $password")
    }
    return password
  }

  private data class Rotation(val direction: Direction, val distance: Int)
  private data class Dial(var point: Int = 50)
}
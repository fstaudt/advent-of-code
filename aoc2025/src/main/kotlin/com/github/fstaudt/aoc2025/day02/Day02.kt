package com.github.fstaudt.aoc2025.day02

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
  Day02().run()
}

class Day02(fileName: String = "day_02.txt") : Day {
  override val input: List<String> = readInputLines(fileName)

  override fun part1(): Long {
    var idRanges = input[0].split(",").map { it.split("-").let { IdRange(Id(it[0]), Id(it[1])) } }
    while (idRanges.any { it.from.length != it.until.length }) {
      idRanges = idRanges.flatMap { it.split() }
    }
    idRanges = idRanges.filter { it.from.id.length % 2 == 0 }
    var sum = 0L
    idRanges.forEach { range ->
      if (range.from.part2() <= range.from.part1() && range.until.part1() > range.from.part1())
        sum += "${range.from.part1()}${range.from.part1()}".toLong()
      if (range.from.part2() <= range.from.part1() && range.until.part1() == range.from.part1() && range.until.part2() >= range.from.part1())
        sum += "${range.from.part1()}${range.from.part1()}".toLong()
      for (part1 in (range.from.part1() + 1)..<range.until.part1()) {
        sum += "${part1}${part1}".toLong()
      }
      if (range.until.part1() != range.from.part1() && range.until.part1() <= range.until.part2())
        sum += "${range.until.part1()}${range.until.part1()}".toLong()
    }
    return sum
  }

  override fun part2(): Long {
    val idRanges = input[0].split(",").map { it.split("-").let { IdRange(Id(it[0]), Id(it[1])) } }
    var sum = 0L
    idRanges.forEach { range ->
      for (number in range.from.number..range.until.number) {
        val id = number.toString()
        for (size in 1..id.length / 2) {
          if (id.isSequenceOf(id.take(size))) {
            sum += number
            break
          }
        }
      }
    }
    return sum
  }

  private fun String.isSequenceOf(pattern: String): Boolean {
    return if (isEmpty()) true
    else if (startsWith(pattern)) return drop(pattern.length).isSequenceOf(pattern)
    else false
  }

  private data class Id(val id: String) {
    val number = id.toLong()
    val length = id.length
    fun part1() = id.take(length / 2).toLong()
    fun part2() = id.drop(length / 2).toLong()
  }

  private data class IdRange(val from: Id, val until: Id) {
    fun split(): List<IdRange> {
      val fromLength = from.length
      return if (fromLength == until.length) listOf(this)
      else listOf(IdRange(from, Id("9".repeat(fromLength))), IdRange(Id("1${"0".repeat(fromLength)}"), until))
    }
  }
}


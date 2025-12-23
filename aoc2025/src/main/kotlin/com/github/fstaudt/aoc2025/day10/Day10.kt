package com.github.fstaudt.aoc2025.day10

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.MatchResultExtensions.group
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.IntSort
import com.microsoft.z3.Model
import com.microsoft.z3.Status.UNSATISFIABLE
import kotlin.Long.Companion.MAX_VALUE
import kotlin.math.min

fun main() {
  Day10().run()
}

class Day10(fileName: String = "day_10.txt") : Day {

  override val input: List<String> = readInputLines(fileName)
  private val machines = input.map { Machine(it) }

  private data class MinUntilJoltages(val count: Long, val min: Long)

  private val minsUntilJoltages = mutableMapOf<String, MinUntilJoltages>()

  override fun part1() = machines.sumOf { it.minUntilLights(it.lights.map { false }, it.buttons, 0) }
  override fun part2() = machines.sumOf { machine -> machine.minUntilJoltages() }

  private fun Machine.minUntilLights(currentLights: List<Boolean>, leftButtons: List<List<Int>>, count: Long): Long {
    if (currentLights == lights) return count
    if (leftButtons.isEmpty()) return MAX_VALUE
    val button = leftButtons[0]
    val newLights = currentLights.mapIndexed { i, light -> if (button.contains(i)) !light else light }
    return min(
      minUntilLights(currentLights, leftButtons.drop(1), count),
      minUntilLights(newLights, leftButtons.drop(1), count + 1)
    )
  }

  private fun Machine.minUntilJoltages(): Long {
    Context().use { context ->
      with(context) {
        // MINIMIZE count
        val optimizer = mkOptimize()
        val count = mkIntConst("count")
        optimizer.MkMinimize(count)
        // CONSTRAINT: count = b1 + b2 + b3
        val z3Buttons = (1..buttons.size).map { b -> mkIntConst("b$b") }
        optimizer.Add(
          mkEq(
            count,
            z3Buttons.drop(1).fold(z3Buttons[0]) { s: Expr<IntSort>, b -> mkAdd(s, b) }
          )
        )
        // CONSTRAINT: forEach b*: b >= 0
        z3Buttons.forEach { optimizer.Add(mkGe(it, mkInt(0))) }
        // CONSTRAINT: forEach j: joltages[j] = b1 + b3
        joltages.forEachIndexed { j, joltage ->
          optimizer.Add(
            mkEq(
              mkInt(joltage),
              buttons.mapIndexedNotNull { b, button -> z3Buttons[b].takeIf { button.contains(j) } }.let {
                it.drop(1).fold(it[0]) { s: Expr<IntSort>, b -> mkAdd(s, b) }
              }
            )
          )
        }
        // SOLVE
        if (optimizer.Check() == UNSATISFIABLE) return 0L
        return optimizer.model.evalAsLong(count)
      }
    }
  }

  private fun Model.evalAsLong(expr: Expr<*>) = eval(expr, false).toString().toLong()
  private data class Machine(private val input: String) {
    companion object {
      private val REGEX = "^\\[(?<lights>[.#]+)] (?<buttons>(\\([\\d,]+\\) )+)\\{(?<joltages>[\\d,]+)}$".toRegex()
    }

    private val matchResult = REGEX.find(input)!!
    val lights = matchResult.group("lights").map { it == '#' }
    val buttons = matchResult.group("buttons").dropLast(1).split(" ").map { button ->
      button.drop(1).dropLast(1).split(",").map { it.toInt() }
    }
    val joltages = matchResult.group("joltages").split(",").map { it.toInt() }
  }
}

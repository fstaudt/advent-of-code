package com.github.fstaudt.aoc2024.day07

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc2024.day07.Day07.Operator.*

fun main() {
    Day07().run()
}

class Day07(fileName: String = "day_07.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val equations = input.map { it.split("[: ]+".toRegex()).map { it.toLong() }.let { Equation(it[0], it.drop(1)) } }

    override fun part1() = equations.filter { it.isValidFor(listOf(MUL, ADD)) }.sumOf { it.testValue }
    override fun part2() = equations.filter { it.isValidFor(listOf(MUL, ADD, PIPE)) }.sumOf { it.testValue }

    data class Equation(val testValue: Long, val numbers: List<Long>) {
        fun isValidFor(operators: List<Operator>, result: Long = numbers[0], index: Int = 1): Boolean {
            return when {
                index >= numbers.size -> result == testValue
                result > testValue -> false
                else -> operators.any { isValidFor(operators, it.compute(result, numbers[index]), index + 1) }
            }
        }
    }

    enum class Operator(val compute: (Long, Long) -> Long) {
        MUL({ a: Long, b: Long -> a * b }),
        ADD({ a: Long, b: Long -> a + b }),
        PIPE({ a: Long, b: Long -> "$a$b".toLong() })
    }
}
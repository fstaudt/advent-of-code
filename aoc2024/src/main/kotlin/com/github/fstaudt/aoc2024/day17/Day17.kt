package com.github.fstaudt.aoc2024.day17

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.MatchResultExtensions.longGroup
import com.github.fstaudt.aoc2024.day17.Day17.InstructionType.*
import java.security.InvalidKeyException
import kotlin.math.pow

fun main() {
    Day17().run()
}

class Day17(fileName: String = "day_17.txt") : Day {
    companion object {
        private val REGISTER_REGEX = """Register (?<id>[ABC]): (?<value>\d+)""".toRegex()
    }

    override val input: List<String> = readInputLines(fileName)
    fun registers() = input.take(3).mapNotNull {
        REGISTER_REGEX.find(it)?.run { Register(groups["id"]!!.value, longGroup("value")) }
    }

    val rawInstructions = input[4].drop(9)

    override fun part1() = 0L.also { println("Part 1: ${part1AsString()}") }
    fun part1AsString() = Program(registers(), rawInstructions).output()

    override fun part2(): Long {
        var initialA = 0L
        var output = " "
        while (output != rawInstructions) {
            if (rawInstructions.endsWith(output)) initialA *= 8 else initialA++
            val registers = registers().also { it.first().value = initialA }
            output = Program(registers, rawInstructions).output()
        }
        return initialA
    }

    data class Register(var id: String, var value: Long)
    data class Program(val registers: List<Register>, val rawInstructions: String) {
        val registerA = registers.first { it.id == "A" }
        val registerB = registers.first { it.id == "B" }
        val registerC = registers.first { it.id == "C" }
        val instructions = rawInstructions.chunked(2).map { it[0].digitToInt() }
        private fun Int.toCombo(): Long = when (this) {
            0, 1, 2, 3 -> toLong()
            4 -> registerA.value
            5 -> registerB.value
            6 -> registerC.value
            else -> throw InvalidKeyException()
        }

        fun output(expected: String? = null): String {
            var output = ""
            var pointer = 0
            while (pointer + 1 < instructions.size) {
                val instruction = instructions[pointer]
                val operand = instructions[pointer + 1]
                pointer += 2
                when (InstructionType.of(instruction)) {
                    ADV -> registerA.value = registerA.value / (2.0).pow(operand.toCombo().toDouble()).toLong()
                    BXL -> registerB.value = registerB.value.xor(operand.toLong())
                    BST -> registerB.value = operand.toCombo() % 8
                    JNZ -> if (registerA.value != 0L) pointer = operand
                    BXC -> registerB.value = registerB.value.xor(registerC.value)
                    BDV -> registerB.value = registerA.value / (2.0).pow(operand.toCombo().toDouble()).toInt()
                    CDV -> registerC.value = registerA.value / (2.0).pow(operand.toCombo().toDouble()).toInt()
                    OUT -> {
                        output = "$output,${operand.toCombo() % 8}"
                        expected?.let { if (!",$it".startsWith(output)) return "" }
                    }
                }
            }
            return output.drop(1)
        }
    }

    enum class InstructionType(val opcode: Int) {
        ADV(0),
        BXL(1),
        BST(2),
        JNZ(3),
        BXC(4),
        OUT(5),
        BDV(6),
        CDV(7);

        companion object {
            fun of(int: Int) = InstructionType.entries.first { it.opcode == int }
        }
    }
}
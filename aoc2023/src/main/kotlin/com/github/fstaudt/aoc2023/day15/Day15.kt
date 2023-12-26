package com.github.fstaudt.aoc2023.day15

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitNotEmpty
import com.github.fstaudt.aoc2023.day15.Day15.Operation.DASH
import com.github.fstaudt.aoc2023.day15.Day15.Operation.EQUALS

fun main() {
    Day15().run()
}

class Day15(fileName: String = "day_15.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val steps = input[0].splitNotEmpty(',')

    override fun part1() = steps.sumOf { it.toHash().toLong() }
    override fun part2() = focusingPower()

    private fun focusingPower(): Long {
        val instructions = steps.map { step ->
            if (step.endsWith("-"))
                Instruction(step.dropLast(1), DASH)
            else
                step.split("=").let { Instruction(it[0], EQUALS, it[1].toInt()) }
        }
        val boxes = List(256) { mutableListOf<Lens>() }
        for (instruction in instructions) {
            val box = boxes[instruction.label.toHash()]
            when (instruction.operation) {
                DASH ->
                    box.removeIf { it.label == instruction.label }

                EQUALS ->
                    if (box.any { it.label == instruction.label }) {
                        val index = box.indexOfFirst { it.label == instruction.label }
                        box[index] = Lens(instruction.label, instruction.focal!!)
                    } else {
                        box.add(Lens(instruction.label, instruction.focal!!))
                    }
            }
        }
        return boxes.mapIndexed { boxNumber, box ->
            box.mapIndexed { lensNumber, lens ->
                (boxNumber + 1) * (lensNumber + 1) * lens.focal
            }.sumOf { it.toLong() }
        }.sumOf { it }
    }

    private fun String.toHash(): Int {
        return fold(0) { hash, c -> ((hash + c.code) * 17).rem(256) }
    }

    data class Instruction(val label: String, val operation: Operation, val focal: Int? = null)
    enum class Operation { EQUALS, DASH }
    data class Lens(val label: String, val focal: Int)
}

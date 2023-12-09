package com.github.fstaudt.aoc2023.day8

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines

fun main() {
    Day8().run()
}

class Day8(fileName: String = "day_8.txt") : Day {
    companion object {
        private val NODE_REGEX = Regex("([0-9A-Z]+) = \\(([0-9A-Z]+), ([0-9A-Z]+)\\)")
    }

    override val input: List<String> = readInputLines(fileName)
    private val instructions = input[0]
    private val nodes = input.drop(2).toNodes()

    override fun part1() = numberOfSteps()

    override fun part2() = numberOfSimultaneousSteps()

    fun leastCommonMultiple(longs: List<Long>): Long = longs.toLeastCommonMultiple()

    private fun numberOfSteps() = nodes["AAA"]!!.toNumberOfStepsUntil { it.id == "ZZZ" }

    private fun numberOfSimultaneousSteps(): Long {
        return nodes.values.filter { it.id.endsWith("A") }
            .map { node -> node.toNumberOfStepsUntil { it.id.endsWith("Z") } }
            .toLeastCommonMultiple()
    }

    private fun Node.toNumberOfStepsUntil(endIsReachedFor: (Node) -> Boolean): Long {
        var node = this
        var steps = 0L
        var nextInstructions = instructions
        while (!endIsReachedFor(node)) {
            node = (if (nextInstructions[0] == 'L') node.left else node.right).let { nodes[it]!! }
            steps++
            nextInstructions = nextInstructions.drop(1).let { it.ifEmpty { instructions } }
        }
        return steps
    }

    private fun List<String>.toNodes(): Map<String, Node> {
        return map { it.toNode() }.associateBy { it.id }
    }

    private fun String.toNode() = NODE_REGEX.find(this)!!.groupValues.let { Node(it[1], it[2], it[3]) }

    private fun List<Long>.toLeastCommonMultiple(): Long {
        return map { it.splitByFactors() }
            .reduce { fl1, fl2 -> fl1 + fl1.fold(fl2) { fl, f -> fl.minus(f) } }
            .reduce { a, b -> a * b }
    }

    private fun Long.splitByFactors(): List<Long> {
        val number = this
        var factor = 2L
        while (factor * factor <= number) {
            if ((number / factor) * factor == number) {
                return listOf(factor) + (number / factor).splitByFactors()
            }
            factor++
        }
        return listOf(number)
    }

    data class Node(val id: String, val left: String, val right: String)
}

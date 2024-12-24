package com.github.fstaudt.aoc2024.day24

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.MatchResultExtensions.group
import com.github.fstaudt.aoc2024.day24.Day24.GateOperator.*
import com.github.fstaudt.aoc2024.day24.Day24.GateOperator.Companion.toGateOperator
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT
import guru.nidi.graphviz.engine.Format.SVG
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Factory.node
import java.io.File

fun main() {
    Day24().run()
}

class Day24(val fileName: String = "day_24.txt") : Day {
    companion object {
        private val GATE_REGEX = """(?<t1>.{3}) (?<op>AND|OR|XOR) (?<t2>.{3}) -> (?<r>.{3})""".toRegex()
    }

    override val input: List<String> = readInputLines(fileName)
    private val initialWires = input.takeWhile { it.isNotEmpty() }
        .map { line -> line.split(":").let { Wire(it[0], it[1].trim().toInt()) } }
    private val gates = input.dropWhile { it.isNotEmpty() }.filter { it.isNotEmpty() }.mapNotNull { line ->
        GATE_REGEX.find(line)?.let { Gate(it.group("t1"), it.toGateOperator(), it.group("t2"), it.group("r")) }
    }

    private fun device() = Device(initialWires, gates)

    override fun part1(): Long {
        with(device()) {
            while (zWires.any { it.value == -1 }) {
                gates.filter { wire(it.r).value == -1 }
                    .filter { wire(it.t1).value != -1 && wire(it.t2).value != -1 }
                    .forEach { gate ->
                        when (gate.operator) {
                            AND -> wire(gate.r).value = wire(gate.t1).value.and(wire(gate.t2).value)
                            XOR -> wire(gate.r).value = wire(gate.t1).value.xor(wire(gate.t2).value)
                            OR -> wire(gate.r).value = wire(gate.t1).value.or(wire(gate.t2).value)
                        }
                    }
            }
            return zWires.map { it.value.toLong() }.reduce { i, j -> 2 * i + j }
        }
    }

    override fun part2() = 0L.also { println("Part 2: ${part2AsString()}") }
    fun part2AsString(): String {
        device().visualizeIn(File("build/png/$fileName-graph.svg"))
        return listOf(
            "btb", "mwp",
            "z30", "rdg",
            "rmj", "z23",
            "z17", "cmv"
        ).sorted().joinToString(",")
    }

    data class Wire(val id: String, var value: Int)
    data class Gate(val t1: String, val operator: GateOperator, val t2: String, val r: String) {
        override fun toString() = "$t1 $operator $t2 -> $r"
        fun toLinks() = listOf(node(t1).linkTo().with(color()), node(t2).linkTo().with(color()))
        private fun color(): Color = when (operator) {
            AND -> Color.RED
            OR -> Color.BLUE
            XOR -> Color.GREEN
        }
    }

    data class Device(val initialWires: List<Wire>, val gates: List<Gate>) {
        private val wires = initialWires + gates.map { Wire(it.r, -1) }
        private val wiresMap = wires.associateBy { it.id }
        val zWires = wires.filter { it.id.startsWith("z") }.sortedByDescending { it.id }

        fun wire(id: String) = wiresMap[id]!!
        fun visualizeIn(file: File) {
            val visualization = graph("device")
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .with(wires.map { wire ->
                    node(wire.id).link(gates.filter { it.r == wire.id }.flatMap { gate -> gate.toLinks() })
                })
            Graphviz.fromGraph(visualization).totalMemory(1024000000).render(SVG).toFile(file)
        }
    }

    enum class GateOperator {
        AND, OR, XOR;

        companion object {
            fun MatchResult.toGateOperator() = GateOperator.valueOf(group("op"))
        }
    }

}


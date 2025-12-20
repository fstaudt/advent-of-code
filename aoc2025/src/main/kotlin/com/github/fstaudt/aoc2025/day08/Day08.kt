package com.github.fstaudt.aoc2025.day08

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day08().run()
}

class Day08(fileName: String = "day_08.txt", val connectionsCount: Int = 1000) : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        val junctionBoxes = junctionBoxes()
        val connections = junctionBoxes.toConnections().sortedBy { it.straightLineDistance }
        connections.take(connectionsCount).forEach { connection ->
            if (connection.from.circuit != connection.until.circuit) {
                junctionBoxes.filter { it.circuit == connection.until.circuit }.forEach {
                    it.circuit = connection.from.circuit
                }
            }
        }
        val circuits = junctionBoxes.groupBy { it.circuit }.values.sortedByDescending { it.size }
        return circuits.take(3).fold(1L) { t, c -> t * c.size }
    }

    override fun part2(): Long {
        val junctionBoxes = junctionBoxes()
        val connections = junctionBoxes.toConnections().sortedBy { it.straightLineDistance }
        connections.forEach { connection ->
            if (connection.from.circuit != connection.until.circuit) {
                junctionBoxes.filter { it.circuit == connection.until.circuit }.forEach {
                    it.circuit = connection.from.circuit
                }
                if (junctionBoxes.map { it.circuit }.distinct().size == 1) {
                    return connection.from.x * connection.until.x
                }
            }
        }
        return 0L
    }

    private fun junctionBoxes() = input.map { JunctionBox(it.split(",")) }

    private fun List<JunctionBox>.toConnections(): List<Connection> {
        return flatMapIndexed { i, fromBox -> take(i).map { Connection(fromBox, it) } }
    }

    private data class JunctionBox(private val positions: List<String>, var circuit: Int = nextCircuit++) {
        companion object {
            var nextCircuit = 1
        }

        val x = positions[0].toLong()
        val y = positions[1].toLong()
        val z = positions[2].toLong()
    }

    private data class Connection(val from: JunctionBox, val until: JunctionBox) {
        val straightLineDistance = (from.x - until.x) * (from.x - until.x) +
                (from.y - until.y) * (from.y - until.y) +
                (from.z - until.z) * (from.z - until.z)
    }
}
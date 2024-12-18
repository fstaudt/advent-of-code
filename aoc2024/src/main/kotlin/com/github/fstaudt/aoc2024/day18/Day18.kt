package com.github.fstaudt.aoc2024.day18

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Direction
import com.github.fstaudt.aoc.shared.DirectedGraph
import com.github.fstaudt.aoc.shared.DirectedGraph.Node
import com.github.fstaudt.aoc.shared.DirectedGraph.Vertex
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.entry
import kotlin.Long.Companion.MAX_VALUE

fun main() {
    Day18().run()
}

class Day18(fileName: String = "day_18.txt", val size: Int = 70, val minBytes: Int = 1024) : Day {
    override val input: List<String> = readInputLines(fileName)
    fun memory(bytes: Int = minBytes): Memory {
        return Memory((0..size).map { i -> (0..size).map { j -> Coordinate(i, j) } }).also { memory ->
            input.take(bytes).forEach {
                it.split(",").let { memory.coordinate(it[0].toInt(), it[1].toInt())!!.corrupted = true }
            }
        }
    }

    override fun part1() = memory().toDirectedGraph().toShortestPathWeight()

    override fun part2(): Long {
        var possible = minBytes
        var impossible = input.size
        while (impossible - possible > 1) {
            val next = (impossible + possible) / 2
            val shortestPathWeight = memory(next).toDirectedGraph().toShortestPathWeight()
            if (shortestPathWeight == MAX_VALUE) {
                impossible = next
            } else {
                possible = next
            }
        }
        return impossible.toLong()
    }

    fun part2AsString(): String = input[part2().toInt() - 1]

    data class Memory(val coordinates: Matrix<Coordinate>) {
        fun coordinate(i: Int, j: Int) = coordinates.entry(i, j)
        fun coordinate(i: Int, j: Int, dir: Direction) = coordinates.entry(i, j, dir)
        fun toDirectedGraph(): DirectedGraph<Coordinate> {
            val nodes = coordinates.flatten().filterNot { it.corrupted }.associateWith { Node(it) }
            val vertices = nodes.values.flatMap { node ->
                Direction.entries.mapNotNull { dir ->
                    nodes[coordinate(node.state.i, node.state.j, dir)]?.takeUnless { it.state.corrupted }
                        ?.let { Vertex(node, it, 1) }
                }
            }
            return DirectedGraph(nodes.values.toList(), vertices, nodes.values.first(), nodes.values.last())
        }
    }

    data class Coordinate(val i: Int, val j: Int) {
        var corrupted: Boolean = false
    }
}
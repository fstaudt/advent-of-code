package com.github.fstaudt.aoc2023.day23

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.graph.Graph
import com.github.fstaudt.aoc.shared.graph.Node
import com.github.fstaudt.aoc.shared.graph.Vertex
import com.github.fstaudt.aoc2023.day23.Day23.TileType.FOREST
import com.github.fstaudt.aoc2023.day23.Day23.TileType.PATH

fun main() {
    Day23().run()
}

class Day23(fileName: String = "day_23.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = stepsOfLongestHike()

    override fun part2() = stepsOfLongestHikeWithClimbingSlopes()

    private fun stepsOfLongestHike() = input.toHikingMap().toDirectedGraph().longestPath()
    private fun stepsOfLongestHikeWithClimbingSlopes() = input.toHikingMap().toGraph().longestPath()

    private fun List<String>.toHikingMap(): List<List<Position>> {
        return mapIndexed { l, line -> line.mapIndexed { c, char -> Position(l, c, TileType.of(char)) } }.also { map ->
            map.flatten().forEach { position ->
                position.adjacentPositions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).mapNotNull { (line, column) ->
                    val nextLine = position.line + line
                    val nextColumn = position.column + column
                    if (nextLine in map.indices && nextColumn in map[0].indices)
                        map[nextLine][nextColumn].takeIf { it.type != FOREST }
                    else null
                }
            }
        }
    }

    private fun List<List<Position>>.toGraph(): Graph {
        val graph = emptyGraph()
        graph.nodes.values.forEach { node ->
            val position = get(node.line)[node.column]
            position.adjacentPositions.forEach { adjacent ->
                var weight = 1L
                var previous = position
                var current = adjacent
                while (current.adjacentPositions.size == 2) {
                    val next = current.adjacentPositions.first { it != previous }
                    previous = current
                    current = next
                    weight++
                }
                graph.nodes[position.id()]!!.vertices += Vertex(position.id(), current.id(), weight)
                graph.nodes[current.id()]!!.vertices += Vertex(current.id(), position.id(), weight)
            }
        }
        return graph
    }

    private fun List<List<Position>>.toDirectedGraph(): Graph {
        val graph = emptyGraph()
        graph.nodes.values.forEach { node ->
            val position = get(node.line)[node.column]
            position.adjacentPositions.forEach { adjacent ->
                var weight = 1L
                var previous = position
                var current = adjacent
                var valid = true
                while (current.adjacentPositions.size == 2) {
                    val next = current.adjacentPositions.first { it != previous }
                    previous = current
                    current = next
                    if (current.type.slope != null) {
                        val currentSlope = (current.line - previous.line) to (current.column - previous.column)
                        valid = valid && (next.type.slope == currentSlope)
                    }
                    weight++
                }
                if (valid) graph.nodes[position.id()]!!.vertices += Vertex(position.id(), current.id(), weight)
            }
        }
        return graph
    }

    private fun List<List<Position>>.emptyGraph(): Graph {
        val start = first().first { it.type == PATH }.let { Node(it.line, it.column) }
        val end = last().last { it.type == PATH }.let { Node(it.line, it.column) }
        val graph = Graph(start, end)
        flatten().filter { it.type != FOREST }.forEach { position ->
            if (position.adjacentPositions.size > 2) {
                graph.nodes += Node(position.line, position.column).let { it.id() to it }
            }
        }
        return graph
    }

    data class Position(
        val line: Int,
        val column: Int,
        val type: TileType,
        var adjacentPositions: List<Position> = emptyList()
    ) {
        fun id() = "$line-$column"
        override fun equals(other: Any?) = (other as? Position)?.let { it.id() == id() } ?: false
        override fun hashCode() = id().hashCode()
    }

    enum class TileType(val char: Char, val slope: Pair<Int, Int>? = null) {
        PATH('.'),
        FOREST('#'),
        DOWN_SLOPE('v', slope = 1 to 0),
        UP_SLOPE('^', slope = -1 to 0),
        LEFT_SLOPE('<', slope = 0 to -1),
        RIGHT_SLOPE('>', slope = 0 to 1),
        ;

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }
}

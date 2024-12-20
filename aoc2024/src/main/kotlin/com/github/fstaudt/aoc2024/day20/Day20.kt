package com.github.fstaudt.aoc2024.day20

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.DirectedGraph
import com.github.fstaudt.aoc.shared.DirectedGraph.Node
import com.github.fstaudt.aoc.shared.Direction
import com.github.fstaudt.aoc.shared.Direction.RIGHT
import com.github.fstaudt.aoc.shared.Direction.UP
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.entry
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf
import com.github.fstaudt.aoc2024.day20.Day20.TileType.*
import kotlin.math.abs

fun main() {
    Day20().run()
}

class Day20(fileName: String = "day_20.txt", private val saved: Int = 100) : Day {
    override val input: List<String> = readInputLines(fileName)
    private val maze = Maze(input.toMatrixOf { Tile(it.i, it.j, TileType.of(it.char)) })
    private val graph = maze.toGraph().also { it.setShortestPathWeights() }

    override fun part1(): Long {
        val nodesMap = graph.nodes.associateBy { it.state }
        return graph.nodes.sumOf { node ->
            Direction.entries.count { dir ->
                when {
                    maze.tile(node.state, dir)?.type != WALL -> false
                    else -> {
                        val shortcut = nodesMap[maze.tile(node.state, dir, 2)]
                        shortcut?.let {
                            it.shortestPathWeight - (node.shortestPathWeight + 2) >= saved
                        } ?: false
                    }
                }
            }
        }.toLong()
    }

    override fun part2(): Long {
        val nodesMap = graph.nodes.associateBy { it.state }
        return graph.nodes.sumOf { node ->
            (-20..20).sumOf { i ->
                (-20 + abs(i)..20 - abs(i)).count { j ->
                    val targetTile = maze.tile(node.state, UP, i)?.let { maze.tile(it, RIGHT, j) }
                    val targetNode = targetTile?.let { nodesMap[it] }
                    targetNode?.let {
                        (it.shortestPathWeight - (node.shortestPathWeight + abs(i) + abs(j)) >= saved)
                    } ?: false
                }
            }
        }.toLong()
    }

    data class Maze(val tiles: Matrix<Tile>) {
        fun tile(tile: Tile, dir: Direction, steps: Int = 1): Tile? {
            return tiles.entry(tile.i + steps * dir.di, tile.j + steps * dir.dj)
        }

        fun toGraph(): DirectedGraph<Tile> {
            val nodes = tiles.flatten().filter { it.type.free }.associateWith { Node(it) }
            val start = nodes[tiles.flatten().first { it.type == START }]!!
            val end = nodes[tiles.flatten().first { it.type == END }]!!
            val vertices = nodes.values.flatMap { node ->
                Direction.entries.mapNotNull { dir ->
                    tile(node.state, dir)?.takeIf { it.type.free }?.let { DirectedGraph.Vertex(node, nodes[it]!!, 1) }
                }
            }
            return DirectedGraph(nodes.values.toList(), vertices, start, end)
        }
    }

    data class Tile(val i: Int, val j: Int, val type: TileType)
    enum class TileType(val char: Char, val free: Boolean) {
        WALL('#', false), FREE('.', true), START('S', true), END('E', true);

        companion object {
            fun of(char: Char) = TileType.entries.first { it.char == char }
        }
    }
}
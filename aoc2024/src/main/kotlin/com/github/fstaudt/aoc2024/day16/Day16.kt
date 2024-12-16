package com.github.fstaudt.aoc2024.day16

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.printToPng
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf
import com.github.fstaudt.aoc2024.day16.Day16.Direction.EAST
import com.github.fstaudt.aoc2024.day16.Day16.TileType.*
import java.awt.Color.*
import kotlin.Long.Companion.MAX_VALUE
import kotlin.math.min

fun main() {
    Day16().run()
}

class Day16(val fileName: String = "day_16.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        val maze = Maze(input.toMatrixOf { Tile(it.i, it.j, TileType.of(it.char)) })
        return maze.graph.toShortestPathWeight()
    }

    override fun part2(): Long {
        val maze = Maze(input.toMatrixOf { Tile(it.i, it.j, TileType.of(it.char)) })
        return maze.graph.toShortestPaths().flatMap { it }.flatMap {
            when (it) {
                is CrossNode -> listOf(it.tile)
                is PathNode -> it.tiles
                else -> emptyList()
            }
        }.distinct().map { it.also { it.shortest = true } }.count().toLong().also {
            maze.printToPng("${fileName}-maze")
        }
    }

    data class Maze(val tiles: Matrix<Tile>) {
        var graph: Graph

        init {
            val start = tiles.flatten().first { it.type == START }
            val end = tiles.flatten().first { it.type == END }
            val crosses = tiles.flatten().filter { tile ->
                tile.type == FREE &&
                    Direction.entries.filter { tile(tile.i + it.di, tile.j + it.dj)?.type?.free == true }.let { dirs ->
                        dirs.size == 4 || dirs.sumOf { 10 * it.di + it.dj } != 0
                    }
            } + start + end
            val crossNodes = crosses.map { CrossNode(it) }
            fun pathsFrom(tile: Tile, path: List<Tile> = listOf(tile)): List<Path> {
                return Direction.entries.filter { dir ->
                    tile(tile.i + dir.di, tile.j + dir.dj).let { next ->
                        next != null && next.type.free && !path.contains(next)
                    }
                }.flatMap { dir ->
                    val next = tile(tile.i + dir.di, tile.j + dir.dj)!!
                    when {
                        crosses.contains(next) -> listOf(Path(dir, path + next))
                        else -> pathsFrom(next, path + next)
                    }
                }
            }

            val paths = crosses.flatMap { pathsFrom(it) }
            val pathNodes = paths.map { PathNode(it.dir, it.tiles) }
            val startNode = crossNodes.first { it.tile == start }
            val vertices = mutableListOf<Vertex>()
            pathNodes.filter { it.tiles.first() == start }.forEach {
                vertices.add(Vertex(startNode, it, (if (it.dir == EAST) 0 else 1000)))
            }
            pathNodes.forEach { pathNode ->
                val last = pathNode.tiles.last()
                val vertexToLast = pathNode.tiles.last().let { last ->
                    Vertex(pathNode, crossNodes.first { it.tile == last }, pathNode.tiles.size - 1)
                }
                val verticesToPath = pathNodes.filter {
                    it.tiles.first() == last && it.dir != pathNode.dir.reversed()
                }.map {
                    var weight = pathNode.tiles.size - 1
                    if (it.dir != pathNode.dir) weight += 1000
                    Vertex(pathNode, it, weight)
                }
                vertices.addAll(verticesToPath + vertexToLast)
            }
            graph = Graph(crossNodes + pathNodes, vertices)
        }

        private fun tile(i: Int, j: Int) = tiles.getOrNull(i)?.getOrNull(j)

        fun printToPng(fileName: String) {
            tiles.printToPng(fileName) {
                when {
                    it.type == WALL -> BLACK
                    it.shortest -> MAGENTA
                    else -> WHITE
                }
            }
        }
    }

    data class Path(val dir: Direction, val tiles: List<Tile>)
    data class Tile(val i: Int, val j: Int, val type: TileType, var shortest: Boolean = false) {
        override fun toString() = "Tile($i,$j)"
    }

    enum class TileType(val char: Char, val free: Boolean) {
        WALL('#', false), FREE('.', true), START('S', true), END('E', true);

        companion object {
            fun of(char: Char) = TileType.entries.first { it.char == char }
        }
    }

    enum class Direction(val di: Int, val dj: Int) {
        NORTH(-1, 0), SOUTH(1, 0), EAST(0, 1), WEST(0, -1);

        fun reversed(): Direction = when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }

    data class Graph(val nodes: List<Node>, val vertices: List<Vertex>) {
        private val start = nodes.first { it is CrossNode && it.tile.type == START }
        private val end = nodes.first { it is CrossNode && it.tile.type == END }

        init {
            nodes.forEach { node ->
                node.fromVertices = vertices.filter { it.from === node }
                node.toVertices = vertices.filter { it.to === node }
            }
        }

        fun toShortestPathWeight(): Long {
            val visitedNodes = mutableSetOf<Node>(start)
            start.shortestPathWeight = 0
            start.visited = true
            while (end.shortestPathWeight == MAX_VALUE) {
                val vertices = visitedNodes.flatMap { node -> node.fromVertices.filter { !it.to.visited } }
                vertices.forEach {
                    it.to.shortestPathWeight = min(it.to.shortestPathWeight, it.from.shortestPathWeight + it.weight)
                }
                vertices.minBy { it.to.shortestPathWeight }.to.also {
                    visitedNodes.add(it)
                    it.visited = true
                }
            }
            return end.shortestPathWeight
        }

        fun toShortestPaths(): List<List<Node>> {
            val visitedNodes = mutableSetOf<Node>(start)
            start.shortestPathWeight = 0
            start.visited = true
            var vertices = visitedNodes.flatMap { node -> node.fromVertices.filter { !it.to.visited } }
            while (vertices.isNotEmpty()) {
                vertices = visitedNodes.flatMap { node -> node.fromVertices.filter { !it.to.visited } }
                vertices.forEach {
                    it.to.shortestPathWeight = min(it.to.shortestPathWeight, it.from.shortestPathWeight + it.weight)
                }
                vertices.minByOrNull { it.to.shortestPathWeight }?.to?.also {
                    visitedNodes.add(it)
                    it.visited = true
                }
            }
            return shortestPathsUntil(end, listOf(end))
        }

        private fun shortestPathsUntil(node: Node, path: List<Node>): List<List<Node>> {
            if (node == start) return listOf(path)
            return vertices.filter { it.to == node && it.from.shortestPathWeight == node.shortestPathWeight - it.weight }
                .flatMap { shortestPathsUntil(it.from, listOf(it.from) + path) }
        }
    }

    interface Node {
        var visited: Boolean
        var shortestPathWeight: Long
        var fromVertices: List<Vertex>
        var toVertices: List<Vertex>
    }

    data class CrossNode(
        val tile: Tile,
        override var visited: Boolean = false, override var shortestPathWeight: Long = MAX_VALUE,
        override var fromVertices: List<Vertex> = emptyList(),
        override var toVertices: List<Vertex> = emptyList()
    ) : Node {
        override fun equals(other: Any?) = (other as? CrossNode)?.tile == tile
        override fun hashCode() = tile.hashCode()
        override fun toString() = "CrossNode(tile=$tile)"

    }

    data class PathNode(
        val dir: Direction, val tiles: List<Tile>,
        override var visited: Boolean = false, override var shortestPathWeight: Long = MAX_VALUE,
        override var fromVertices: List<Vertex> = emptyList(),
        override var toVertices: List<Vertex> = emptyList()
    ) : Node {

        override fun equals(other: Any?) = (other as? PathNode)?.let { it.dir == dir && it.tiles == tiles } ?: false
        override fun hashCode() = 34 * dir.hashCode() + tiles.hashCode()
        override fun toString() = "PathNode(dir=$dir, tiles=$tiles)"
    }

    data class Vertex(val from: Node, val to: Node, val weight: Int)
}
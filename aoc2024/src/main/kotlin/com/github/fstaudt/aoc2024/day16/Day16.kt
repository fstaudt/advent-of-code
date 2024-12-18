package com.github.fstaudt.aoc2024.day16

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.DirectedGraph
import com.github.fstaudt.aoc.shared.DirectedGraph.Node
import com.github.fstaudt.aoc.shared.DirectedGraph.Vertex
import com.github.fstaudt.aoc.shared.Direction
import com.github.fstaudt.aoc.shared.Direction.RIGHT
import com.github.fstaudt.aoc.shared.Direction.UP
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.printToPng
import com.github.fstaudt.aoc.shared.MatrixExtensions.toMatrixOf
import com.github.fstaudt.aoc2024.day16.Day16.TileType.*
import java.awt.Color.*

fun main() {
    Day16().run()
}

class Day16(val fileName: String = "day_16.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        val maze = Maze(input.toMatrixOf { Tile(it.i, it.j, TileType.of(it.char)) })
        return maze.toGraph().toShortestPathWeight()
    }

    override fun part2(): Long {
        val maze = Maze(input.toMatrixOf { Tile(it.i, it.j, TileType.of(it.char)) })
        val shortestPaths = maze.toGraph().toShortestPaths().flatMap { shortestPath ->
            shortestPath.windowed(2).flatMap { (from, to) ->
                mutableListOf(from.state.tile).also {
                    var nextTile: Tile = from.state.tile
                    while (nextTile != to.state.tile && nextTile.type != END) {
                        nextTile = maze.tile(nextTile.i, nextTile.j, from.state.dir)!!
                        it.add(nextTile)
                    }
                }
            }
        }.distinct()
        shortestPaths.forEach { it.shortest = true }
        maze.printToPng("${fileName}-maze")
        return shortestPaths.count().toLong()
    }

    data class Maze(val tiles: Matrix<Tile>) {
        fun tile(i: Int, j: Int, dir: Direction, steps: Int = 1): Tile? {
            return tiles.getOrNull(i + steps * dir.di)?.getOrNull(j + steps * dir.dj)
        }

        fun toGraph(): DirectedGraph<State> {
            val crosses = tiles.flatten().filter { it.type.free }.associateWith { tile ->
                Direction.entries.associateWith { dir -> Node(State(tile, dir)) }
            }.filter { (tile, dirs) ->
                dirs.filterKeys { tile(tile.i, tile.j, it)?.type?.free == true }.let {
                    it.size != 2 || it.keys.toList().let { it[0] != it[1].reversed() }
                }
            }
            val vertices = mutableListOf<Vertex<State>>()
            val end = Node(State(Tile(0, 0, FINAL), UP))
            crosses.values.flatMap { it.values }.forEach { node ->
                val (tile, dir) = node.state
                crosses[tile]?.get(dir.toClockwise())?.let { vertices.add(Vertex(node, it, 1000)) }
                crosses[tile]?.get(dir.toCounterClockwise())?.let { vertices.add(Vertex(node, it, 1000)) }
                var steps = 1
                var nextCross = crosses[tile(tile.i, tile.j, dir, steps)]?.get(dir)
                while (nextCross == null && tile(tile.i, tile.j, dir, steps)?.type != WALL) {
                    nextCross = crosses[tile(tile.i, tile.j, dir, ++steps)]?.get(dir)
                }
                if (nextCross != null) {
                    vertices.add(Vertex(node, nextCross, steps))
                }
                if (node.state.tile.type == END) {
                    vertices.add(Vertex(node, end, 0))
                }
            }
            val nodes = crosses.values.flatMap { it.values } + end
            val start = nodes.first { it.state.tile.type == START && it.state.dir == RIGHT }
            return DirectedGraph(nodes, vertices, start, end)
        }

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

    data class State(val tile: Tile, val dir: Direction)
    data class Tile(val i: Int, val j: Int, val type: TileType) {
        var shortest: Boolean = false
    }

    enum class TileType(val char: Char, val free: Boolean) {
        WALL('#', false),
        FREE('.', true),
        START('S', true),
        END('E', true),
        FINAL('X', false);

        companion object {
            fun of(char: Char) = TileType.entries.first { it.char == char }
        }
    }
}
package com.github.fstaudt.aoc2023.day25

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitNotEmpty
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT
import guru.nidi.graphviz.engine.Format.PNG
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Factory.node
import java.io.File
import kotlin.random.Random.Default.nextInt

fun main() {
    Day25().run()
}

class Day25(val fileName: String = "day_25.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = productOfSeparatedGroupCounts()
    override fun part2() = 0L

    private fun productOfSeparatedGroupCounts(): Long {
        var minimalGlobalCut = emptyList<Vertex>()
        var attempt = 1
        while (minimalGlobalCut.size != 3) {
            // Karger's algorithm
            val graph = input.toLines().toGraph()
            while (graph.nodes.count() > 2) {
                graph.contractRandomVertex()
            }
            minimalGlobalCut = graph.vertices
            println("Attempt #${++attempt}: ${minimalGlobalCut.size}")
        }
        val graph = input.toLines().toGraph()
        val cuts = minimalGlobalCut.map { cut -> cut.id.split("-").let { Vertex(cut.id, it[0], it[1]) } }
        graph.vertices.removeAll(cuts)
        val count = graph.countFrom(graph.nodes.values.first().id)
        return count * (graph.nodes.count() - count)
    }

    fun visualize(width: Int, height: Int) {
        val graph = input.toLines().toGraph()
        val vertices = graph.vertices
        val visualization = graph(fileName)
            .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
            .with(graph.nodes.values.map { node ->
                node(node.id).link(vertices.filter { it.origin == node.id }.map { vertex -> node(vertex.target) })
            })
        Graphviz.fromGraph(visualization).totalMemory(1024000000).width(width).height(height)
            .render(PNG).toFile(File("$fileName.png"))
    }

    private fun List<Line>.toGraph(): Graph {
        val graph = Graph()
        forEach { line ->
            graph.nodes += line.node to Node(line.node)
            graph.nodes += line.connectedNodes.map { it to Node(it) }
            line.connectedNodes.forEach {
                graph.vertices += Vertex("${line.node}-$it", line.node, it)
            }
        }
        return graph
    }

    private fun List<String>.toLines() = map { line -> line.split(":").let { Line(it[0], it[1].splitNotEmpty()) } }

    data class Line(val node: String, val connectedNodes: List<String>)

    data class Graph(
        val nodes: MutableMap<String, Node> = mutableMapOf(),
        val vertices: MutableList<Vertex> = mutableListOf()
    ) {
        fun contractRandomVertex() {
            val vertexToContract = vertices[nextInt(vertices.size - 1)]
            val node = nodes[vertexToContract.origin]!!
            val removedNode = nodes.remove(vertexToContract.target)!!
            vertices.filter { it.origin == removedNode.id || it.target == removedNode.id }.forEach {
                when {
                    it.origin == node.id -> vertices.remove(it)
                    it.target == node.id -> vertices.remove(it)
                    it.origin == removedNode.id -> it.origin = node.id
                    it.target == removedNode.id -> it.target = node.id
                }
            }
        }

        fun countFrom(origin: String): Long {
            nodes.forEach { it.value.visited = false }
            countFrom(nodes[origin]!!)
            return nodes.count { it.value.visited }.toLong()
        }

        private fun countFrom(node: Node) {
            if (node.visited) return
            node.visited = true
            vertices.filter { it.origin == node.id }.forEach { countFrom(nodes[it.target]!!) }
            vertices.filter { it.target == node.id }.forEach { countFrom(nodes[it.origin]!!) }
        }
    }

    data class Node(val id: String, var visited: Boolean = false)
    data class Vertex(val id: String, var origin: String, var target: String)
}



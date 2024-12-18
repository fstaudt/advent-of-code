package com.github.fstaudt.aoc.shared

import kotlin.Long.Companion.MAX_VALUE
import kotlin.math.min

data class DirectedGraph<S>(
    val nodes: List<Node<S>>,
    val vertices: List<Vertex<S>>,
    val start: Node<S> = nodes.first(),
    val end: Node<S> = nodes.last()
) {

    init {
        nodes.forEach { node ->
            node.fromVertices = vertices.filter { it.from === node }
            node.toVertices = vertices.filter { it.to === node }
        }
    }

    fun toShortestPathWeight(): Long {
        setShortestPathWeights()
        return end.shortestPathWeight
    }

    fun toShortestPaths(): List<List<Node<S>>> {
        setShortestPathWeights()
        return shortestPathsUntil(end)
    }

    fun setShortestPathWeights() {
        start.shortestPathWeight = 0
        start.visited = true
        val visitedNodes = mutableSetOf<Node<S>>(start)
        var vertices = start.fromVertices
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
    }

    private fun shortestPathsUntil(node: Node<S>, path: List<Node<S>> = listOf(node)): List<List<Node<S>>> {
        if (node == start) return listOf(path)
        return vertices.filter { it.to == node && it.from.shortestPathWeight == node.shortestPathWeight - it.weight }
            .flatMap { shortestPathsUntil(it.from, listOf(it.from) + path) }
    }

    data class Node<S>(val state: S) {
        var visited: Boolean = false
        var shortestPathWeight: Long = MAX_VALUE
        var fromVertices: List<Vertex<S>> = emptyList()
        var toVertices: List<Vertex<S>> = emptyList()
    }

    data class Vertex<S>(val from: Node<S>, val to: Node<S>, val weight: Int)
}


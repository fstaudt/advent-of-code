package com.github.fstaudt.aoc2023.day23.graph

import kotlin.math.max

data class Graph(
    val start: Node,
    val end: Node,
    val nodes: MutableMap<String, Node> = mutableMapOf(start.id() to start, end.id() to end)
) {
    fun longestPath(from: Node = start, to: Node = end, path: Set<Vertex> = emptySet()): Long {
        val targets = from.vertices.filter { it.target !in path.map { it.origin } }
        if (targets.isEmpty()) {
            return if (path.last().target != to.id()) 0 else path.sumOf { it.weight }
        } else {
            var longest = 0L
            targets.forEach { vertex ->
                val next = nodes[vertex.target]!!
                longest = max(longest, longestPath(next, to, path + vertex))
            }
            return longest
        }
    }


}

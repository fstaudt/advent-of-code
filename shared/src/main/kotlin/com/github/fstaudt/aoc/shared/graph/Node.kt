package com.github.fstaudt.aoc.shared.graph

data class Node(val line: Int, val column: Int, val vertices: MutableSet<Vertex> = mutableSetOf()) {
    fun id() = "$line-$column"
    override fun equals(other: Any?) = (other as? Node)?.let { it.id() == id() } ?: false
    override fun hashCode() = id().hashCode()
}

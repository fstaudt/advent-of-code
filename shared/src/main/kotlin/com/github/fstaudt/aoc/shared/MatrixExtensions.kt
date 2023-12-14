package com.github.fstaudt.aoc.shared

object MatrixExtensions {
    fun <E> List<List<E>>.flipMatrix() = List(get(0).size) { c -> map { it[c] } }
    fun <E> List<List<E>>.tiltClockwise() = List(get(0).size) { c -> map { it[c] }.reversed() }
}

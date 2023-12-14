package com.github.fstaudt.aoc2023.shared

object MatrixExtensions {
    internal fun <E> List<List<E>>.flipMatrix() = List(get(0).size) { c -> map { it[c] } }
    internal fun <E> List<List<E>>.tiltClockwise() = List(get(0).size) { c -> map { it[c] }.reversed() }
}

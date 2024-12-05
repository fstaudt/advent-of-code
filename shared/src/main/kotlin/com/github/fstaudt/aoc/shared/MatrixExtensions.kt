package com.github.fstaudt.aoc.shared

object MatrixExtensions {
    fun <E> Matrix<E>.flipMatrix() = List(get(0).size) { c -> map { it[c] } }
    fun <E> Matrix<E>.tiltClockwise() = List(get(0).size) { c -> map { it[c] }.reversed() }
    fun <E> Matrix<E>.forEachEntry(op: (E) -> Unit) = forEach { it.forEach(op) }
}

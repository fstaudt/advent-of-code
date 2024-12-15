package com.github.fstaudt.aoc.shared

object MatrixExtensions {
    fun <E> Matrix<E>.flipMatrix() = List(get(0).size) { c -> map { it[c] } }
    fun <E> Matrix<E>.tiltClockwise() = List(get(0).size) { c -> map { it[c] }.reversed() }
    fun <E> Matrix<E>.forEachEntry(op: (E) -> Unit) = forEach { it.forEach(op) }
    fun <E> Matrix<E>.print(char: (E) -> Char) = forEach { it.forEach { (print(char(it))) }; println() }
    fun <E> List<String>.toMatrixOf(op: (Element) -> E): Matrix<E> {
        return mapIndexed { i, line -> line.mapIndexed { j, c -> op(Element(i, j, c)) } }
    }

    data class Element(val i: Int, val j: Int, val char: Char)
}

package com.github.fstaudt.aoc.shared

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO

object MatrixExtensions {
    fun <E> Matrix<E>.flip() = List(get(0).size) { c -> map { it[c] } }
    fun <E> Matrix<E>.tiltClockwise() = List(get(0).size) { c -> map { it[c] }.reversed() }
    fun <E> Matrix<E>.forEachEntry(op: (E) -> Unit) = forEach { it.forEach(op) }
    fun <E> Matrix<E>.entry(i: Int, j: Int): E? = getOrNull(i)?.getOrNull(j)
    fun <E> Matrix<E>.entry(i: Int, j: Int, dir: Direction, steps: Int = 1): E? {
        return getOrNull(i + steps * dir.di)?.getOrNull(j + steps * dir.dj)
    }

    fun <E> List<String>.toMatrixOf(op: (Element) -> E): Matrix<E> {
        return mapIndexed { i, line -> line.mapIndexed { j, c -> op(Element(i, j, c)) } }
    }

    fun <E> Matrix<E>.print(char: (E) -> Char) = forEach { it.forEach { (print(char(it))) }; println() }
    fun <E> Matrix<E>.printToPng(fileName: String, color: (E) -> Color) {
        val image = BufferedImage(get(0).size, size, TYPE_INT_RGB)
        forEachIndexed { x, line ->
            line.forEachIndexed { y, char ->
                image.setRGB(y, x, color(char).rgb)
            }
        }
        ImageIO.write(image, "png", File("build/png/$fileName.png").also { it.parentFile.mkdirs() })
    }

    data class Element(val i: Int, val j: Int, val char: Char)
}

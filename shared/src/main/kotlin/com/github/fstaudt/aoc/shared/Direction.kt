package com.github.fstaudt.aoc.shared

enum class Direction(val di: Int, val dj: Int) {
    UP(-1, 0), DOWN(1, 0), RIGHT(0, 1), LEFT(0, -1);

    fun reversed(): Direction = when(this) {
        UP -> DOWN
        DOWN -> UP
        RIGHT -> LEFT
        LEFT -> RIGHT
    }

    fun toClockwise(): Direction = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    fun toCounterClockwise(): Direction = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
    }
}

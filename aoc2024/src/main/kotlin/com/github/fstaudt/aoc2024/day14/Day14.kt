package com.github.fstaudt.aoc2024.day14

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.MatchResultExtensions.intGroup
import com.github.fstaudt.aoc.shared.Matrix
import com.github.fstaudt.aoc.shared.MatrixExtensions.flip
import com.github.fstaudt.aoc.shared.MatrixExtensions.printToPng
import java.awt.Color.BLACK
import java.awt.Color.WHITE
import kotlin.math.sign

fun main() {
    Day14().run()
}

class Day14(val fileName: String = "day_14.txt", val bathroom: Bathroom = Bathroom(101, 103)) : Day {
    companion object {
        val ROBOT_REGEX = """p=(?<x>\d+),(?<y>\d+) v=(?<dx>-?\d+),(?<dy>-?\d+)""".toRegex()
    }

    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        val robots = input.map {
            ROBOT_REGEX.find(it).run { Robot(intGroup("x"), intGroup("y"), intGroup("dx"), intGroup("dy")) }
        }.also { bathroom.robots = it }
        robots.forEach { while (it.moves < 100) it.moveIn(bathroom) }
        return robots.countInQuadrant(-1, 1) * robots.countInQuadrant(-1, -1) *
            robots.countInQuadrant(1, -1) * robots.countInQuadrant(1, 1)
    }

    override fun part2(): Long {
        val robots = input.map {
            ROBOT_REGEX.find(it).run { Robot(intGroup("x"), intGroup("y"), intGroup("dx"), intGroup("dy")) }
        }.also { bathroom.robots = it }
        val robot = robots[0]
        while (robot.moves < 10000 && !bathroom.isChristmasTree()) {
            robots.forEach { it.moveIn(bathroom) }
        }
        bathroom.display().positions.flip().printToPng("${fileName}-${robot.moves}") { if (it.busy) BLACK else WHITE }
        return robot.moves
    }

    fun List<Robot>.countInQuadrant(upDown: Int, leftRight: Int) = count {
        (it.x - (bathroom.x / 2)).sign == upDown &&
            (it.y - (bathroom.y / 2)).sign == leftRight
    }.toLong()

    data class Robot(var x: Int, var y: Int, val dx: Int, val dy: Int, var moves: Long = 0) {
        fun moveIn(bathroom: Bathroom) {
            moves++
            x += dx
            if (x < 0) x += bathroom.x
            if (x >= bathroom.x) x -= bathroom.x
            y += dy
            if (y < 0) y += bathroom.y
            if (y >= bathroom.y) y -= bathroom.y
        }

        override fun toString() = "Robot($x, $y, $dx, $dy, $moves)"
    }

    data class Bathroom(val x: Int, val y: Int, var robots: List<Robot> = emptyList()) {
        fun isChristmasTree() = display().isChristmasTree()
        fun display(): Display {
            val positions = (0..<x).map { i -> (0..<y).map { j -> Position(i, j) } }.also { positions ->
                robots.forEach { positions[it.x][it.y].busy = true }
            }
            return Display(positions)
        }
    }

    data class Display(val positions: Matrix<Position>) {
        fun position(i: Int, j: Int) = positions.getOrNull(i)?.getOrNull(j)
        fun isChristmasTree(): Boolean {
            return positions.flatten().filter { it.busy }.any { isChristmasTreeAt(it) }
        }

        fun isChristmasTreeAt(robot: Position): Boolean {
            if (isTrunk(robot, 22)) {
                if (position(robot.x, robot.y - 1)?.busy == true) return false
                if (position(robot.x - 1, robot.y)?.busy == true) return false
                if (position(robot.x + 1, robot.y)?.busy == true) return false
                var height = 1
                var span = 0
                var nextSpan = position(robot.x, robot.y + height)?.let { span(it) } ?: 0
                // check tree top
                var topHeight = 1
                while (span < nextSpan) {
                    if (nextSpan - span != 1) return false
                    topHeight++
                    height++
                    span = nextSpan
                    nextSpan = span(position(robot.x, robot.y + height))
                }
                if (topHeight < 5) return false
                // check tree branches
                if (nextSpan < 1) return false
                var branches = 1
                var branchHeight = 1
                height++
                span = nextSpan
                nextSpan = span(position(robot.x, robot.y + height))
                while (span != nextSpan) {
                    while (span < nextSpan) {
                        if (nextSpan - span != 1) return false
                        branchHeight++
                        height++
                        span = nextSpan
                        nextSpan = span(position(robot.x, robot.y + height))
                    }
                    if (branchHeight < 5) return false
                    if (nextSpan < 1) return false
                    branches++
                    height++
                    branchHeight = 1
                    span = nextSpan
                    nextSpan = span(position(robot.x, robot.y + height))
                }
                if (branches < 4) return false
                // check tree foot
                var footHeight = 1
                while (nextSpan != 0) {
                    if (span != nextSpan) return false
                    footHeight++
                    height++
                    span = nextSpan
                    nextSpan = span(position(robot.x, robot.y + height))
                }
                return footHeight >= 3
            }
            return false
        }

        fun isTrunk(robot: Position, size: Int): Boolean {
            return (1..<size).all { position(robot.x, robot.y + it)?.busy == true }
        }

        fun span(robot: Position?): Int {
            if (robot == null) return 0
            if (position(robot.x, robot.y)?.busy != true) return 0
            val leftSpan = (1..51).takeWhile { position(robot.x - it, robot.y)?.busy == true }.size
            val rightSpan = (1..51).takeWhile { position(robot.x + it, robot.y)?.busy == true }.size
            return leftSpan.takeIf { it == rightSpan } ?: 0
        }
    }

    data class Position(val x: Int, val y: Int, var busy: Boolean = false)
}
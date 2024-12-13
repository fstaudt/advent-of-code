package com.github.fstaudt.aoc2024.day13

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.IntExpr
import com.microsoft.z3.Model
import com.microsoft.z3.Status.UNSATISFIABLE
import kotlin.Long.Companion.MAX_VALUE

fun main() {
    Day13().run()
}

class Day13(fileName: String = "day_13.txt") : Day {

    companion object {
        val BUTTON = """Button [AB]: X\+(?<dx>\d+), Y\+(?<dy>\d+)""".toRegex()
        val PRIZE = """Prize: X=(?<x>\d+), Y=(?<y>\d+)""".toRegex()
    }

    override val input: List<String> = readInputLines(fileName)

    override fun part1(): Long {
        val claws = input.chunked(4).map { Claw(Button.from(it[0]), Button.from(it[1]), Prize.from(it[2])) }
        return claws.mapNotNull { it.cheapestWayToWin() }.sumOf { it.cost }
    }

    override fun part2(): Long {
        val offset = 10000000000000L
        val claws = input.chunked(4).map { Claw(Button.from(it[0]), Button.from(it[1]), Prize.from(it[2], offset)) }
        return claws.mapNotNull { it.cheapestWayToWinZ3() }.sumOf { it.cost }
    }

    data class Claw(val a: Button, val b: Button, val prize: Prize) {
        fun cheapestWayToWin(): Way? {
            var cheapestWay: Way? = null
            for (na in 0L..100) {
                var nb = (prize.x - na * a.dx) / b.dx
                if (na * a.dx + nb * b.dx == prize.x && na * a.dy + nb * b.dy == prize.y) {
                    val cost = 3L * na + nb
                    if (cost < (cheapestWay?.cost ?: MAX_VALUE)) {
                        cheapestWay = Way(na, nb, cost)
                    }
                }
                if (na * a.dx > prize.x && na * a.dy > prize.y) {
                    break
                }
            }
            return cheapestWay
        }

        fun cheapestWayToWinZ3(): Way? {
            Context().use { context ->
                with(context) {
                    val solver = mkSolver()
                    val way = Z3Way(mkIntConst("na"), mkIntConst("nb"), mkIntConst("cost"))
                    solver.add(
                        mkEq(mkAdd(mkMul(way.na, mkInt(a.dx)), mkMul(way.nb, mkInt(b.dx))), mkInt(prize.x)),
                        mkEq(mkAdd(mkMul(way.na, mkInt(a.dy)), mkMul(way.nb, mkInt(b.dy))), mkInt(prize.y)),
                        mkEq(mkAdd(mkMul(mkInt(3), way.na), way.nb), way.cost),
                    )
                    if (solver.check() == UNSATISFIABLE) return null
                    return with(solver.model) {
                        Way(evalAsLong(way.na), evalAsLong(way.nb), evalAsLong(way.cost))
                    }
                }
            }
        }

        private fun Model.evalAsLong(expr: Expr<*>) = eval(expr, false).toString().toLong()
    }

    data class Button(val dx: Long, val dy: Long) {
        companion object {
            fun from(line: String) = BUTTON.find(line)!!.let {
                Button(it.groups["dx"]!!.value.toLong(), it.groups["dy"]!!.value.toLong())
            }
        }
    }

    data class Prize(val x: Long, val y: Long) {
        companion object {
            fun from(line: String, offset: Long = 0) = PRIZE.find(line)!!.let {
                Prize(it.groups["x"]!!.value.toLong() + offset, it.groups["y"]!!.value.toLong() + offset)
            }
        }
    }

    data class Way(val na: Long, val nb: Long, val cost: Long)
    data class Z3Way(val na: IntExpr, val nb: IntExpr, val cost: IntExpr)
}
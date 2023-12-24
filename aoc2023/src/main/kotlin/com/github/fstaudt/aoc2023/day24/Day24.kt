package com.github.fstaudt.aoc2023.day24

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.splitLongs
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.IntExpr
import com.microsoft.z3.Model
import com.microsoft.z3.Status.UNSATISFIABLE

fun main() {
    Day24().run()
}

class Day24(fileName: String = "day_24.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = countIntersections(200000000000000 to 400000000000000)
    override fun part2() = sumRockPositions()

    fun countIntersections(range: Pair<Long, Long>): Long {
        val hailStones = input.toHailStones()
        var count = 0L
        for (i in hailStones.indices) {
            for (j in (i + 1..<hailStones.size)) {
                val h1 = hailStones[i]
                val h2 = hailStones[j]
                intersection(h1, h2).let { (x, y) ->
                    if (range.includes(x) && range.includes(y) && h1.aimsAt(x, y) && h2.aimsAt(x, y)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    private fun intersection(l1: HailStone, l2: HailStone): Intersection {
        val x = (l1.vel.x * (l2.pos.y - l1.pos.y - 1.0 * l2.vel.y * l2.pos.x / l2.vel.x) / l1.vel.y + l1.pos.x) /
                (1 - 1.0 * l1.vel.x * l2.vel.y / (l1.vel.y * l2.vel.x))
        val y = 1.0 * l1.vel.y * (x - l1.pos.x) / l1.vel.x + l1.pos.y
        return Intersection(x, y)
    }

    data class Intersection(val x: Double, val y: Double)

    private fun Pair<Long, Long>.includes(double: Double) = double >= first && double <= second

    private fun sumRockPositions(): Long {
        val hailStones = input.toHailStones()
        Context().use {
            with(it) {
                val solver = mkSolver()
                val rock = Z3HailStone(
                    Z3Position(mkIntConst("px"), mkIntConst("py"), mkIntConst("pz")),
                    Z3Velocity(mkIntConst("vx"), mkIntConst("vy"), mkIntConst("vz"))
                )
                hailStones.take(3).forEachIndexed { index, h ->
                    val time = mkIntConst("t$index")
                    solver.add(
                        mkEq(
                            mkAdd(rock.pos.x, mkMul(time, rock.vel.x)),
                            mkAdd(mkInt(h.pos.x), mkMul(time, mkInt(h.vel.x)))
                        ),
                        mkEq(
                            mkAdd(rock.pos.y, mkMul(time, rock.vel.y)),
                            mkAdd(mkInt(h.pos.y), mkMul(time, mkInt(h.vel.y)))
                        ),
                        mkEq(
                            mkAdd(rock.pos.z, mkMul(time, rock.vel.z)),
                            mkAdd(mkInt(h.pos.z), mkMul(time, mkInt(h.vel.z)))
                        ),
                    )
                }
                solver.check().let { if (it == UNSATISFIABLE) return 0 }
                val solved = with(solver.model) {
                    HailStone(
                        0,
                        Position(evalAsLong(rock.pos.x), evalAsLong(rock.pos.y), evalAsLong(rock.pos.z)),
                        Velocity(evalAsLong(rock.vel.x), evalAsLong(rock.vel.y), evalAsLong(rock.vel.z))
                    )
                }
                return solved.pos.x + solved.pos.y + solved.pos.z
            }
        }
    }

    private fun Model.evalAsLong(expr: Expr<*>) = eval(expr, false).toString().toLong()

    private fun List<String>.toHailStones(): List<HailStone> {
        return mapIndexed { index, it ->
            it.split('@').let { hailstone ->
                val position = hailstone[0].splitLongs(',').let { Position(it[0], it[1], it[2]) }
                val velocity = hailstone[1].splitLongs(',').let { Velocity(it[0], it[1], it[2]) }
                HailStone(index, position, velocity)
            }
        }
    }

    data class HailStone(val index: Int, val pos: Position, val vel: Velocity) {
        override fun equals(other: Any?) = (other as? HailStone)?.index == index
        override fun hashCode(): Int = index.hashCode()
        fun aimsAt(x: Double, y: Double): Boolean {
            val aimsAtInX = when {
                vel.x < 0 -> pos.x >= x
                vel.x > 0 -> pos.x <= x
                else -> true
            }
            val aimsAtInY = when {
                vel.y < 0 -> pos.y >= y
                vel.y > 0 -> pos.y <= y
                else -> true
            }
            return aimsAtInX && aimsAtInY
        }
    }

    data class Position(val x: Long, val y: Long, val z: Long)
    data class Velocity(val x: Long, val y: Long, val z: Long)
    data class Z3HailStone(val pos: Z3Position, val vel: Z3Velocity)
    data class Z3Position(val x: IntExpr, val y: IntExpr, val z: IntExpr)
    data class Z3Velocity(val x: IntExpr, val y: IntExpr, val z: IntExpr)
}

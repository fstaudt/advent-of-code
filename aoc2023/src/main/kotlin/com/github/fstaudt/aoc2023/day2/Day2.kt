package com.github.fstaudt.aoc2023.day2

import com.github.fstaudt.aoc2023.shared.Day
import com.github.fstaudt.aoc2023.shared.readInputLines
import kotlin.math.max

fun main() {
    Day2().run()
}


class Day2(fileName: String = "day_2.txt") : Day {

    override val input: List<String> = readInputLines(fileName)

    override fun part1() = input.sumIdOfPossibleGames()

    override fun part2() = input.sumPowersOfMinimalSet()

    private fun List<String>.sumIdOfPossibleGames() = map { it.toGame() }.filter { it.isValid() }.sumOf { it.id }
    private fun List<String>.sumPowersOfMinimalSet() = map { it.toGame() }.sumOf { it.toMinimalSet().toPower() }

    private fun String.toGame(): Game {
        return split(":").let { game ->
            val id = Regex("Game (\\d+)").find(game[0])!!.groupValues[1].toLong()
            val subsets = game[1].split(";").map { it.toSubset() }
            Game(id, subsets)
        }
    }

    private fun String.toSubset(): Subset {
        return split(",").map { it.toSingleColor() }.reduce { subset, color -> subset.plus(color) }
    }

    private fun String.toSingleColor(): Subset {
        return Regex(" *(\\d+) (red|green|blue)").find(this)!!.let {
            val count = it.groupValues[1].toLong()
            when (it.groupValues[2]) {
                "red" -> Subset(count, 0, 0)
                "green" -> Subset(0, count, 0)
                else -> Subset(0, 0, count)
            }
        }
    }

    data class Game(val id: Long, val subsets: List<Subset>) {
        fun isValid() = subsets.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
        fun toMinimalSet() = subsets.reduce { acc, subset -> acc.max(subset) }
    }

    data class Subset(val red: Long, val green: Long, val blue: Long) {
        fun plus(subset: Subset) = Subset(red + subset.red, green + subset.green, blue + subset.blue)
        fun max(subset: Subset) = Subset(max(red, subset.red), max(green, subset.green), max(blue, subset.blue))
        fun toPower() = red * green * blue
    }
}







package com.github.fstaudt.aoc2023.shared

interface LongDay {

    val input: List<Any>

    fun part1(): Long

    fun part2(): Long

    fun run() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

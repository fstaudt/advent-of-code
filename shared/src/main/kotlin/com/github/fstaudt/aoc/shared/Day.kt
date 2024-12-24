package com.github.fstaudt.aoc.shared

import java.time.Duration.between
import java.time.LocalDateTime.now

interface Day {

    val input: List<Any>

    fun part1(): Long

    fun part2(): Long

    fun run() {
        val start = now()
        println("Part 1: ${part1()}")
        println("Duration: ${between(start, now()).toMillis()}ms")
        println("Part 2: ${part2()}")
        println("Duration: ${between(start, now()).toMillis()}ms")
    }
}

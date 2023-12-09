package com.github.fstaudt.aoc.model

typealias CompletionParts = Map<Int, CompletionPart>

object CompletionPartsFunctions {
    fun CompletionParts.part1() = get(1)
    fun CompletionParts.part2() = get(2)
}

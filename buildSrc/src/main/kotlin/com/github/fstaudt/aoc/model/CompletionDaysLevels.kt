package com.github.fstaudt.aoc.model

typealias CompletionDayLevels = Map<Int, CompletionParts>

object CompletionDaysLevelsFunctions {
    fun CompletionDayLevels.day(day: Int) = get(day+1)
}

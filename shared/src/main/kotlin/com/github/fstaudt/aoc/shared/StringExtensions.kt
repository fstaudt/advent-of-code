package com.github.fstaudt.aoc.shared

object StringExtensions {
    fun String.splitNotEmpty(delimiter: Char = ' '): List<String> = split(delimiter).filter { it.isNotEmpty() }
    fun String.splitLongs(delimiter: Char = ' '): List<Long> = splitNotEmpty(delimiter).map { it.trim().toLong() }
    fun String.splitInts(delimiter: Char = ' '): List<Int> = splitNotEmpty(delimiter).map { it.trim().toInt() }
    fun String.toGroupValue(pattern: String, i: Int): String = pattern.toRegex().find(this)!!.groupValues[i]
    fun String.toGroupValues(pattern: String): List<String> = pattern.toRegex().find(this)!!.groupValues
    fun List<String>.flip() = get(0).mapIndexed { index, _ -> joinToString("") { "${it[index]}" } }
}

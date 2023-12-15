package com.github.fstaudt.aoc.shared

object StringExtensions {
    fun String.splitNotEmpty(delimiter: Char = ' '): List<String> = split(delimiter).filter { it.isNotEmpty() }
    fun String.splitLongs(delimiter: Char = ' '): List<Long> = splitNotEmpty(delimiter).map { it.toLong() }
    fun String.splitInts(delimiter: Char = ' '): List<Int> = splitNotEmpty(delimiter).map { it.toInt() }
    fun String.toGroupValue(pattern: String, i: Int): String = Regex(pattern).find(this)!!.groupValues[i]
    fun List<String>.flip() = get(0).mapIndexed { index, _ -> joinToString("") { "${it[index]}" } }
}
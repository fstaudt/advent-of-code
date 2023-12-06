package com.github.fstaudt.aoc2023.shared

internal fun String.splitNotEmpty(delimiter: Char = ' '): List<String> = split(delimiter).filter { it.isNotEmpty() }
internal fun String.splitInts(delimiter: Char = ' '): List<Int> = splitNotEmpty(delimiter).map { it.toInt() }
internal fun String.splitLongs(delimiter: Char = ' '): List<Long> = splitNotEmpty(delimiter).map { it.toLong() }
internal fun String.toGroupValue(pattern: String, i: Int): String = Regex(pattern).find(this)!!.groupValues[i]

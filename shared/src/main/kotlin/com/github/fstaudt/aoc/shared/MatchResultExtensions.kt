package com.github.fstaudt.aoc.shared

object MatchResultExtensions {
    fun MatchResult.group(group: String) = groups[group]!!.value
    fun MatchResult?.intGroup(group: String) = this?.groups?.get(group)?.value?.toInt() ?: 0
    fun MatchResult?.longGroup(group: String) = this?.groups?.get(group)?.value?.toLong() ?: 0
}
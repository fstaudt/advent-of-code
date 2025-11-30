package com.github.fstaudt.aoc.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Leaderboard(
    val event: String,
    @param:JsonProperty("day1_ts") val day1Timestamp: String?,
    @param:JsonProperty("owner_id") val ownerId: String,
    val members: Map<String, Member>,
) {
    fun owner() = members[ownerId]?.name() ?: "(anonymous user #$ownerId)"
    fun sortedBy(selector: (Member) -> CompletionPart?): List<Member> {
        return members.values.sortedBy { selector(it)?.starIndex ?: Int.MAX_VALUE }
    }
    fun numberOfDays() = members.values.maxOf { it.completionDayLevels.size }
}

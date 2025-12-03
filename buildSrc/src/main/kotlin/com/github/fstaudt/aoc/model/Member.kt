package com.github.fstaudt.aoc.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Member(
    val id: String,
    val name: String?,
    val stars: Int,
    @param:JsonProperty("last_star_ts") val lastStarTimestamp: Int,
    @param:JsonProperty("local_score") val localScore: Int,
    @param:JsonProperty("completion_day_level") val completionDayLevels: CompletionDayLevels,
    @JsonIgnore var localDailyScore: Int = 0,
    @JsonIgnore val localDailyScores: MutableList<Int> = mutableListOf(),
    @JsonIgnore val rankings: MutableList<Int?> = mutableListOf(),
) {
    fun name() = name ?: "(anonymous user #$id)"
}

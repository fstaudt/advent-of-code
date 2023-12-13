package com.github.fstaudt.aoc.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CompletionPart(
    @JsonProperty("get_star_ts") val starTimestamp: Long,
    @JsonProperty("star_index") val starIndex: Int,
) {
    fun ifBefore(limit: Long): CompletionPart? = takeIf { starTimestamp < limit }
}

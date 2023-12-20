package com.github.fstaudt.aoc.shared

import com.github.fstaudt.aoc.shared.LongExtensions.toLeastCommonMultiple
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class LongExtensionsTest {

    companion object {
        @JvmStatic
        fun arrangementsTestData() = listOf(
            Arguments.of(listOf(2L, 3L, 5L), 30),
            Arguments.of(listOf(2L, 3L, 10L), 30),
            Arguments.of(listOf(2L, 3L, 4L), 12),
            Arguments.of(listOf(2L, 2L, 4L), 4),
            Arguments.of(listOf(4L, 4L, 10L), 20),
        )
    }

    @ParameterizedTest
    @MethodSource("arrangementsTestData")
    fun `leastCommonMultiple should produce expected outputs`(longs: List<Long>, result: Long) {
        assertThat(longs.toLeastCommonMultiple()).isEqualTo(result)
    }
}

package com.github.fstaudt.aoc2023.day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day21Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day21("example_day21.txt").countGardenPlotsReachedAfter(6)).isEqualTo(16)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day21().part1()).isEqualTo(3709)
    }

    companion object {
        @JvmStatic
        fun countGardenPlotTestData() = listOf(
            Arguments.of(6, 16),
            Arguments.of(10, 50),
            Arguments.of(50, 1594),
//            Arguments.of(100, 6536),
//            Arguments.of(200, 26538),
//            Arguments.of(400, 106776),
//            Arguments.of(500, 167004),
            Arguments.of(1000, 668697),
            Arguments.of(5000, 16733044),
        )
    }

    @ParameterizedTest
    @MethodSource("countGardenPlotTestData")
    fun `part 2 should produce expected result for examples`(steps: Long, result: Long) {
        assertThat(Day21("example_day21.txt").countGardenPlotsReachedOnInfiniteGardenAfter(steps)).isEqualTo(result)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day21().part2()).isEqualTo(617361073602319)
    }
}

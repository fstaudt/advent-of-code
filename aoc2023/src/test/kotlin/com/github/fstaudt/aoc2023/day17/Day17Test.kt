package com.github.fstaudt.aoc2023.day17

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class Day17Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day17("example_day17.txt").part1()).isEqualTo(102)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day17().part1()).isEqualTo(755)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day17("example_day17.txt").part2()).isEqualTo(94)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day17().part2()).isEqualTo(881)
    }

    companion object {
        @JvmStatic
        fun minimalHeatLossTestData() = listOf(
            of(listOf("12", "34"), 2 + 4L),
            of(
                listOf(
                    "241",
                    "321",
                    "325"
                ),
                3 + 2 + 1 + 5L
            ),
            of(
                listOf(
                    "2413",
                    "3215",
                    "3255"
                ),
                3 + 2 + 1 + 5 + 5L
            ),
            of(
                listOf(
                    "241343231",
                    "321545353",
                    "325524565"
                ),
                4 + 1 + 1 + 5 + 4 + 5 + 3 + 2 + 3 + 1 + 3 + 5L
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("minimalHeatLossTestData")
    fun `minimalHeatLoss should produce expected results`(city: List<String>, result: Long) {
        assertThat(Day17().minimalHeatLoss(city)).isEqualTo(result)
    }
}

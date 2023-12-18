package com.github.fstaudt.aoc2023.day18

import com.github.fstaudt.aoc2023.day18.Day18.Dig
import com.github.fstaudt.aoc2023.day18.Day18.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class Day18Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day18("example_day18.txt").part1()).isEqualTo(62)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day18().part1()).isEqualTo(70253)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day18("example_day18.txt").part2()).isEqualTo(952408144115)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day18().part2()).isEqualTo(131265059885080)
    }

    companion object {
        @JvmStatic
        fun lagoonSizeTestData() = listOf(
            of(
                47, listOf(
                    Dig(RIGHT, 2), Dig(DOWN, 2), Dig(RIGHT, 2), Dig(DOWN, 1), Dig(RIGHT, 2),
                    Dig(UP, 3), Dig(RIGHT, 2), Dig(DOWN, 5), Dig(LEFT, 8), Dig(UP, 5),
                )
            ),
            of(
                6 + 9 + 13 + 14 + 14 + 14,
                listOf(
                    Dig(RIGHT, 2), Dig(DOWN, 2), Dig(RIGHT, 3), Dig(UP, 2), Dig(RIGHT, 2),
                    Dig(DOWN, 2), Dig(RIGHT, 2), Dig(DOWN, 1), Dig(RIGHT, 2), Dig(UP, 2),
                    Dig(RIGHT, 2), Dig(DOWN, 4), Dig(LEFT, 13), Dig(UP, 5),
                )
            ),
            of(
                6 + 9 + 15 + 16 + 16 + 16,
                listOf(
                    Dig(RIGHT, 2),
                    Dig(DOWN, 2),
                    Dig(RIGHT, 3),
                    Dig(UP, 2),
                    Dig(RIGHT, 2),
                    Dig(DOWN, 2),
                    Dig(RIGHT, 2),
                    Dig(DOWN, 1),
                    Dig(RIGHT, 2),
                    Dig(UP, 1),
                    Dig(RIGHT, 2),
                    Dig(UP, 1),
                    Dig(RIGHT, 2),
                    Dig(DOWN, 4),
                    Dig(LEFT, 15),
                    Dig(UP, 5),
                )
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("lagoonSizeTestData")
    fun `lagoonSize should produce expected result for inputs`(result: Long, digs: List<Dig>) {
        assertThat(Day18().countLagoonSizeFor(digs)).isEqualTo(result)
    }
}

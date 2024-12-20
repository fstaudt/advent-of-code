package com.github.fstaudt.aoc2024.day20

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day20Test {
    @ParameterizedTest
    @CsvSource(
        "14, 2, 44",
        "14, 4, 30",
        "2, 6, 16",
        "4, 8, 14",
        "2, 10, 10",
        "3, 12, 8",
        "1, 20, 5",
        "1, 36, 4",
        "1, 38, 3",
        "1, 40, 2",
        "1, 64, 1",
        "0, 66, 0",
    )
    fun `part 1 should produce expected result for example`(count: Long, saved: Int, result: Long) {
        assertThat(count).isLessThanOrEqualTo(result)
        assertThat(Day20("example_day20.txt", saved).part1()).isEqualTo(result)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day20().part1()).isEqualTo(1365)
    }

    @ParameterizedTest
    @CsvSource(
        "32, 50, 285",
        "31, 52, 253",
        "29, 54, 222",
        "39, 56, 193",
        "25, 58, 154",
        "23, 60, 129",
        "20, 62, 106",
        "19, 64, 86",
        "12, 66, 67",
        "14, 68, 55",
        "12, 70, 41",
        "22, 72, 29",
        "4, 74, 7",
        "3, 76, 3",
        "0, 78, 0",
    )
    fun `part 2 should produce expected result for example`(count: Long, saved: Int, result: Long) {
        assertThat(count).isLessThanOrEqualTo(result)
        assertThat(Day20("example_day20.txt", saved).part2()).isEqualTo(result)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        // 1093171: too high
        assertThat(Day20().part2()).isEqualTo(986082)
    }
}
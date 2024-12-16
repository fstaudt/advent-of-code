package com.github.fstaudt.aoc2024.day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day16Test {
    @ParameterizedTest
    @CsvSource(
        "example_day16.txt, 7036",
        "example_day16_2.txt, 11048"
    )
    fun `part 1 should produce expected result for example`(fileName: String, result: Long) {
        assertThat(Day16(fileName).part1()).isEqualTo(result)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day16().part1()).isEqualTo(78428)
    }

    @ParameterizedTest
    @CsvSource(
        "example_day16.txt, 45",
        "example_day16_2.txt, 64"
    )
    fun `part 2 should produce expected result for example`(fileName: String, result: Long) {
        assertThat(Day16(fileName).part2()).isEqualTo(result)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day16().part2()).isEqualTo(463)
    }
}
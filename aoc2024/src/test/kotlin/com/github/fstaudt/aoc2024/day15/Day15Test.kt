package com.github.fstaudt.aoc2024.day15

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day15Test {
    @ParameterizedTest
    @CsvSource(
        "example_day15.txt, 10092",
        "example_day15_2.txt, 2028",
    )
    fun `part 1 should produce expected result for example`(file: String, result: Long) {
        assertThat(Day15(file).part1()).isEqualTo(result)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day15().part1()).isEqualTo(1349898)
    }

    @ParameterizedTest
    @CsvSource(
        "example_day15_3.txt, 618",
        "example_day15.txt, 9021",
    )
    fun `part 2 should produce expected result for example`(file: String, result: Long) {
        assertThat(Day15(file).part2()).isEqualTo(result)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day15().part2()).isEqualTo(1376686)
    }
}
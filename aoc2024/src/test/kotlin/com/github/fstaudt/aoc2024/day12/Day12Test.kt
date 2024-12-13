package com.github.fstaudt.aoc2024.day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day12Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day12("example_day12.txt").part1()).isEqualTo(1930)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day12().part1()).isEqualTo(1456082)
    }

    @ParameterizedTest
    @CsvSource(
        "example_day12.txt, 1206",
        "example_day12_2.txt, 80",
        "example_day12_3.txt, 236",
        "example_day12_4.txt, 368"
    )
    fun `part 2 should produce expected result for example`(file: String, result: Long) {
        assertThat(Day12(file).part2()).isEqualTo(result)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day12().part2()).isEqualTo(872382)
    }
}
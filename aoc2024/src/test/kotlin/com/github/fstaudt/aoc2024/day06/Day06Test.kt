package com.github.fstaudt.aoc2024.day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day06Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day06("example_day06.txt").part1()).isEqualTo(41)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day06().part1()).isEqualTo(5199)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day06("example_day06.txt").part2()).isEqualTo(6)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day06().part2()).isEqualTo(1915)
    }
}
package com.github.fstaudt.aoc2024.day04

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day04Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day04("example_day04.txt").part1()).isEqualTo(18)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day04().part1()).isEqualTo(2543)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day04("example_day04.txt").part2()).isEqualTo(9)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day04().part2()).isEqualTo(1930)
    }
}
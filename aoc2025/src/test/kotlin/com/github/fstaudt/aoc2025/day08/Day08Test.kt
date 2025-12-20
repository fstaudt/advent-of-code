package com.github.fstaudt.aoc2025.day08

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day08Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day08("example_day08.txt", 10).part1()).isEqualTo(40)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day08().part1()).isEqualTo(63920)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day08("example_day08.txt").part2()).isEqualTo(25272)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day08().part2()).isEqualTo(1026594680)
    }
}
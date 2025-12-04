package com.github.fstaudt.aoc2025.day04

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day04Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day04("example_day04.txt").part1()).isEqualTo(13)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day04().part1()).isEqualTo(1578)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day04("example_day04.txt").part2()).isEqualTo(43)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day04().part2()).isEqualTo(10132)
    }
}
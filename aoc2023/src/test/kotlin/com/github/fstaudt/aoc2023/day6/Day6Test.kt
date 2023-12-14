package com.github.fstaudt.aoc2023.day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day6Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day6("example_day6.txt").part1()).isEqualTo(288)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day6().part1()).isEqualTo(1660968)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day6("example_day6.txt").part2()).isEqualTo(71503)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day6().part2()).isEqualTo(26499773)
    }
}

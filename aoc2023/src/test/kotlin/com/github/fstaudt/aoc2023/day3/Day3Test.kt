package com.github.fstaudt.aoc2023.day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3Test {
    companion object {
        private const val EXAMPLE = "example_day3.txt"
    }

    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day3(EXAMPLE).part1()).isEqualTo(4361)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day3(EXAMPLE).part2()).isEqualTo(467835)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day3().part1()).isEqualTo(509115)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day3().part2()).isEqualTo(75220503)
    }
}

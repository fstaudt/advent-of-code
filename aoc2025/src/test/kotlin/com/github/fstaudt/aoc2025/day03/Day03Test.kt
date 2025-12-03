package com.github.fstaudt.aoc2025.day03

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day03Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day03("example_day03.txt").part1()).isEqualTo(357)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day03().part1()).isEqualTo(17193)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day03("example_day03.txt").part2()).isEqualTo(3121910778619)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day03().part2()).isEqualTo(171297349921310)
    }
}
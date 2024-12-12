package com.github.fstaudt.aoc2024.day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day11("example_day11.txt").part1()).isEqualTo(55312)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day11().part1()).isEqualTo(189092)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day11("example_day11.txt").part2()).isEqualTo(65601038650482)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day11().part2()).isEqualTo(224869647102559)
    }
}
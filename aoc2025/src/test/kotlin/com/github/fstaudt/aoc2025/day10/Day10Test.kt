package com.github.fstaudt.aoc2025.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day10("example_day10.txt").part1()).isEqualTo(7)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day10().part1()).isEqualTo(527)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day10("example_day10.txt").part2()).isEqualTo(33)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day10().part2()).isEqualTo(19810)
    }
}
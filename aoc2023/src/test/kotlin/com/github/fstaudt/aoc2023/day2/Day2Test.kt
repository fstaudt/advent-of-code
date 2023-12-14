package com.github.fstaudt.aoc2023.day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day2Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day2("example_day2.txt").part1()).isEqualTo(8)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day2().part1()).isEqualTo(2439)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day2("example_day2.txt").part2()).isEqualTo(2286)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day2().part2()).isEqualTo(63711)
    }
}

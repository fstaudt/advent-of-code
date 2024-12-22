package com.github.fstaudt.aoc2024.day22

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day22Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day22("example_day22.txt").part1()).isEqualTo(37327623)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day22().part1()).isEqualTo(12759339434)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day22("example_day22_2.txt").part2()).isEqualTo(23)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day22().part2()).isEqualTo(1405)
    }
}
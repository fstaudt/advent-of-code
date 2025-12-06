package com.github.fstaudt.aoc2025.day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day06Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day06("example_day06.txt").part1()).isEqualTo(4277556)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day06().part1()).isEqualTo(5667835681547)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day06("example_day06.txt").part2()).isEqualTo(3263827)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day06().part2()).isEqualTo(9434900032651)
    }
}
package com.github.fstaudt.aoc2024.day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day13("example_day13.txt").part1()).isEqualTo(480)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day13().part1()).isEqualTo(29517)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day13("example_day13.txt").part2()).isEqualTo(875318608908)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day13().part2()).isEqualTo(103570327981381)
    }
}
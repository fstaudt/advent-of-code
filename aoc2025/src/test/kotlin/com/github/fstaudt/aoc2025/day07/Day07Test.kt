package com.github.fstaudt.aoc2025.day07

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day07Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day07("example_day07.txt").part1()).isEqualTo(21)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day07().part1()).isEqualTo(1649)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day07("example_day07.txt").part2()).isEqualTo(40)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day07().part2()).isEqualTo(16937871060075)
    }
}
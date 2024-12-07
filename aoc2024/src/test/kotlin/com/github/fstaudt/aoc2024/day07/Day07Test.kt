package com.github.fstaudt.aoc2024.day07

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day07Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day07("example_day07.txt").part1()).isEqualTo(3749)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day07().part1()).isEqualTo(1298103531759)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day07("example_day07.txt").part2()).isEqualTo(11387)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day07().part2()).isEqualTo(140575048428831)
    }
}
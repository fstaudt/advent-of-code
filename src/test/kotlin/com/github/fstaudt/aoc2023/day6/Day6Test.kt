package com.github.fstaudt.aoc2023.day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day6Test {

    @Test
    fun `part 1 should produce expected result`() {
        assertThat(Day6().part1()).isEqualTo(1660968)
    }

    @Test
    fun `part 2 should produce expected result`() {
        assertThat(Day6().part2()).isEqualTo(26499773)
    }
}

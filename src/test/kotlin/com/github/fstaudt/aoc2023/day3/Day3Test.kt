package com.github.fstaudt.aoc2023.day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3Test {

    @Test
    fun `part 1 should produce expected result`() {
        assertThat(Day3().part1()).isEqualTo(509115)
    }

    @Test
    fun `part 2 should produce expected result`() {
        assertThat(Day3().part2()).isEqualTo(75220503)
    }
}

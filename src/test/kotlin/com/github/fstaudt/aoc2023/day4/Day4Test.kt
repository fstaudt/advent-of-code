package com.github.fstaudt.aoc2023.day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day4Test {

    @Test
    fun `part 1 should produce expected result`() {
        assertThat(Day4().part1()).isEqualTo(15205)
    }

    @Test
    fun `part 2 should produce expected result`() {
        assertThat(Day4().part2()).isEqualTo(6189740)
    }
}

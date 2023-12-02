package com.github.fstaudt.aoc2023.day1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day1Test {

    @Test
    fun `part 1 should produce expected result`() {
        assertThat(Day1().part1()).isEqualTo(54953)
    }

    @Test
    fun `part 2 should produce expected result`() {
        assertThat(Day1().part2()).isEqualTo(53868)
    }
}

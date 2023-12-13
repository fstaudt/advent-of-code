package com.github.fstaudt.aoc2023.day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day4Test {

    companion object {
        private const val EXAMPLE = "example_day4.txt"
    }

    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day4(EXAMPLE).part1()).isEqualTo(13)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day4(EXAMPLE).part2()).isEqualTo(30)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day4().part1()).isEqualTo(15205)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day4().part2()).isEqualTo(6189740)
    }
}

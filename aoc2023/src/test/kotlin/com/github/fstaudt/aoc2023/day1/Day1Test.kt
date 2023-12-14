package com.github.fstaudt.aoc2023.day1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day1Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day1("example_day1_part1.txt").part1()).isEqualTo(142)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day1().part1()).isEqualTo(54953)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day1("example_day1_part2.txt").part2()).isEqualTo(281)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day1().part2()).isEqualTo(53868)
    }
}

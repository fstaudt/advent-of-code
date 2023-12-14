package com.github.fstaudt.aoc2023.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day10("example_day10_part1.txt").part1()).isEqualTo(8)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day10().part1()).isEqualTo(7030)
    }

    @Test
    fun `part 2 should produce expected result for examples`() {
        assertThat(Day10("example_day10_part2.txt").part2()).isEqualTo(4)
        assertThat(Day10("example_day10_part2_2.txt").part2()).isEqualTo(4)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day10().part2()).isEqualTo(285)
    }
}

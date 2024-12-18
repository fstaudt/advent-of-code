package com.github.fstaudt.aoc2024.day18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day18Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day18("example_day18.txt", 6, 12).part1()).isEqualTo(22)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day18().part1()).isEqualTo(250)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day18("example_day18.txt", 6, 12).part2AsString()).isEqualTo("6,1")
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day18().part2AsString()).isEqualTo("56,8")
    }
}
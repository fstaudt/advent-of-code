package com.github.fstaudt.aoc2023.day18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day18Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day18("example_day18.txt").part1()).isEqualTo(62)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day18().part1()).isEqualTo(70253)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day18("example_day18.txt").part2()).isEqualTo(0)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day18().part2()).isEqualTo(0)
    }
}

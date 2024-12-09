package com.github.fstaudt.aoc2024.day09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day09Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day09("example_day09.txt").part1()).isEqualTo(1928)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day09().part1()).isEqualTo(6201130364722)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day09("example_day09.txt").part2()).isEqualTo(2858)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day09().part2()).isEqualTo(6221662795602)
    }
}
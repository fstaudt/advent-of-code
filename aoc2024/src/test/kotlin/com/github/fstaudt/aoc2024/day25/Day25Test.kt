package com.github.fstaudt.aoc2024.day25

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day25Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day25("example_day25.txt").part1()).isEqualTo(3)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day25().part1()).isEqualTo(3196)
    }
}
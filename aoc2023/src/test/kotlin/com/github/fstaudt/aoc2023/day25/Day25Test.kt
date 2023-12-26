package com.github.fstaudt.aoc2023.day25

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day25Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        Day25("example_day25.txt").visualize(500, 500)
        assertThat(Day25("example_day25.txt").part1()).isEqualTo(54)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        // Day25().visualize(10000, 1000)
        assertThat(Day25().part1()).isEqualTo(582692)
    }
}

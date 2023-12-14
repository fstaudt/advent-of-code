package com.github.fstaudt.aoc2023.day9

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day9Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day9("example_day9.txt").part1()).isEqualTo(114)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day9().part1()).isEqualTo(2105961943)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day9("example_day9.txt").part2()).isEqualTo(2)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day9().part2()).isEqualTo(1019)
    }
}

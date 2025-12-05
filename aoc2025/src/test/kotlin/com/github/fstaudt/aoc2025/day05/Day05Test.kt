package com.github.fstaudt.aoc2025.day05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day05("example_day05.txt").part1()).isEqualTo(3)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day05().part1()).isEqualTo(773)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day05("example_day05.txt").part2()).isEqualTo(14)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day05().part2()).isLessThan(332272015470278).isEqualTo(332067203034711)
    }
}
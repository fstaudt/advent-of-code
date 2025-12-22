package com.github.fstaudt.aoc2025.day09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day09Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day09("example_day09.txt").part1()).isEqualTo(50)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day09().part1()).isEqualTo(4755429952)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day09("example_day09.txt").part2()).isEqualTo(24)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day09().part2())
            .isGreaterThan(1429417152)
            .isLessThan(2687179704)
            .isGreaterThan(1429564036)
            .isEqualTo(1429596008)
    }
}
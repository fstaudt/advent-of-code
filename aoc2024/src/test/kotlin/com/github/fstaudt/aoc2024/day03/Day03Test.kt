package com.github.fstaudt.aoc2024.day03

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day03Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day03("example_day03.txt").part1()).isEqualTo(161)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day03().part1()).isEqualTo(164730528)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day03("example_day03.txt").part2()).isEqualTo(48)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day03().part2()).isEqualTo(70478672)
    }
}
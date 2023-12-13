package com.github.fstaudt.aoc2023.day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13Test {

    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day13("example_day13.txt").part1()).isEqualTo(405)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day13().part1()).isEqualTo(34821)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day13("example_day13.txt").part2()).isEqualTo(400)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day13().part2()).isEqualTo(36919)
    }
}

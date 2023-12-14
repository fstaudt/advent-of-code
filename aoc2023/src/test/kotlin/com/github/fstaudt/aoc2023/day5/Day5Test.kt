package com.github.fstaudt.aoc2023.day5

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day5Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day5("example_day5.txt").part1()).isEqualTo(35)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day5().part1()).isEqualTo(535088217)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day5("example_day5.txt").part2()).isEqualTo(46)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day5().part2()).isEqualTo(51399228)
    }
}

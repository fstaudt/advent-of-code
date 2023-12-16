package com.github.fstaudt.aoc2023.day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day16Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day16("example_day16.txt").part1()).isEqualTo(46)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day16().part1()).isEqualTo(7210)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day16("example_day16.txt").part2()).isEqualTo(51)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day16().part2()).isEqualTo(7673)
    }
}

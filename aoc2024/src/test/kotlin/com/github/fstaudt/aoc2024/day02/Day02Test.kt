package com.github.fstaudt.aoc2024.day02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day02Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day02("example_day02.txt").part1()).isEqualTo(2)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day02().part1()).isEqualTo(224)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day02("example_day02.txt").part2()).isEqualTo(4)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day02().part2()).isEqualTo(293)
    }
}
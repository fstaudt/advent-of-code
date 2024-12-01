package com.github.fstaudt.aoc2024.day01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day01Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day01("example_day01.txt").part1()).isEqualTo(11)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day01().part1()).isEqualTo(2378066)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day01("example_day01.txt").part2()).isEqualTo(31)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day01().part2()).isEqualTo(18934359)
    }
}
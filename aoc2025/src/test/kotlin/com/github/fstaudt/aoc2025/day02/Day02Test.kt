package com.github.fstaudt.aoc2025.day02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day02Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day02("example_day02.txt").part1()).isEqualTo(1227775554)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day02().part1()).isGreaterThan(18654731796).isGreaterThan(18875014125).isEqualTo(18893502033)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day02("example_day02.txt").part2()).isEqualTo(4174379265)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day02().part2()).isEqualTo(26202168557)
    }
}
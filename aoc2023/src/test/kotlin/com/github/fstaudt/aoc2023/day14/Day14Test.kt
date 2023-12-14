package com.github.fstaudt.aoc2023.day14

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day14Test {

    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day14("example_day14.txt").part1()).isEqualTo(136)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day14().part1()).isEqualTo(111339)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day14("example_day14.txt").part2()).isEqualTo(64)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day14().part2()).isEqualTo(93736)
    }
}

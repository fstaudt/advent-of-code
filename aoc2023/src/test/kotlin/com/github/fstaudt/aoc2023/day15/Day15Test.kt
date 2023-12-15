package com.github.fstaudt.aoc2023.day15

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day15Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day15("example_day15.txt").part1()).isEqualTo(1320)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day15().part1()).isEqualTo(504036)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day15("example_day15.txt").part2()).isEqualTo(145)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day15().part2()).isEqualTo(295719)
    }
}

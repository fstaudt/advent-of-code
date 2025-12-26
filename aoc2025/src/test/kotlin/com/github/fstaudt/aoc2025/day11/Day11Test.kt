package com.github.fstaudt.aoc2025.day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day11("example_day11.txt").part1()).isEqualTo(5)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day11().part1()).isEqualTo(543)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day11("example_day11_part2.txt").part2()).isEqualTo(2)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day11().part2()).isEqualTo(479511112939968)
    }
}
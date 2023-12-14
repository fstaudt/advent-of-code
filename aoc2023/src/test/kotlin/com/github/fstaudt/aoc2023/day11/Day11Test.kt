package com.github.fstaudt.aoc2023.day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day11("example_day11.txt").part1()).isEqualTo(374)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day11().part1()).isEqualTo(9403026)
    }

    @Test
    fun `part 2 should produce expected result for examples`() {
        assertThat(Day11("example_day11.txt").sumShortestPathLengths(2)).isEqualTo(374)
        assertThat(Day11("example_day11.txt").sumShortestPathLengths(10)).isEqualTo(1030)
        assertThat(Day11("example_day11.txt").sumShortestPathLengths(100)).isEqualTo(8410)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day11().part2()).isEqualTo(543018317006)
    }
}

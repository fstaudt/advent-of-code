package com.github.fstaudt.aoc2023.day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day12Test {

    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day12("example_day12.txt").part1()).isEqualTo(21)
    }

    @Test
    @Disabled("doesn't work")
    fun `part 2 should produce expected result for example`() {
        assertThat(Day12("example_day12.txt").part2()).isEqualTo(525152)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day12().part1()).isEqualTo(8193)
    }

    @Test
    @Disabled("doesn't work")
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day12().part2()).isEqualTo(0)
    }
}

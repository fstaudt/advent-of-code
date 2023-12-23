package com.github.fstaudt.aoc2023.day23

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day23Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day23("example_day23.txt").part1()).isEqualTo(94)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day23().part1()).isEqualTo(2318)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day23("example_day23.txt").part2()).isEqualTo(154)
    }

    @Test
    fun `part 2 should produce expected result for examples`() {
        assertThat(Day23("example_day23_2.txt").part2()).isEqualTo(14)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day23().part2()).isEqualTo(6426)
    }
}

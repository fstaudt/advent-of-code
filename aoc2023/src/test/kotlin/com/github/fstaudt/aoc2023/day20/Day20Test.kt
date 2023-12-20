package com.github.fstaudt.aoc2023.day20

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day20Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day20("example_day20.txt").part1()).isEqualTo(32000000)
        assertThat(Day20("example_day20_2.txt").part1()).isEqualTo(11687500)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day20().part1()).isEqualTo(896998430)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day20().part2()).isEqualTo(236095992539963)
    }
}

package com.github.fstaudt.aoc2024.day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day21("example_day21.txt").part1()).isEqualTo(126384)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        // 175970: too low
        assertThat(Day21().part1()).isEqualTo(176650)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day21("example_day21.txt").part2()).isEqualTo(154115708116294)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day21().part2()).isEqualTo(217698355426872)
    }
}
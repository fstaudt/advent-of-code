package com.github.fstaudt.aoc2023.day19

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day19Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day19("example_day19.txt").part1()).isEqualTo(19114)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day19().part1()).isEqualTo(386787)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day19("example_day19.txt").part2()).isEqualTo(167409079868000)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day19().part2()).isEqualTo(131029523269531)
    }
}

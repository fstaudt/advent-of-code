package com.github.fstaudt.aoc2024.day24

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day24Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day24("example_day24.txt").part1()).isEqualTo(4)
        assertThat(Day24("example_day24_2.txt").part1()).isEqualTo(2024)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day24().part1()).isEqualTo(55920211035878)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day24().part2AsString()).isEqualTo("btb,cmv,mwp,rdg,rmj,z17,z23,z30")
    }
}
package com.github.fstaudt.aoc2023.day24

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day24Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day24("example_day24.txt").countIntersections(7L to 27)).isEqualTo(2)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day24().part1()).isEqualTo(14046)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day24("example_day24.txt").part2()).isEqualTo(47)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day24().part2()).isEqualTo(808107741406756)
    }
}

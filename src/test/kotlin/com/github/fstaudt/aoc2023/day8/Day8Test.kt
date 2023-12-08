package com.github.fstaudt.aoc2023.day8

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day8Test {

    companion object {
        private const val EXAMPLE_PART1 = "example_day8_part1.txt"
        private const val EXAMPLE_PART2 = "example_day8_part2.txt"
    }

    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day8(EXAMPLE_PART1).part1()).isEqualTo(6)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day8(EXAMPLE_PART2).part2()).isEqualTo(6)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day8().part1()).isEqualTo(17287)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day8().part2()).isEqualTo(18625484023687)
    }

    @Test
    fun `toLCM should produce expected output`() {
        assertThat(Day8().leastCommonMultiple(listOf(2L, 3L, 5L))).isEqualTo(30)
        assertThat(Day8().leastCommonMultiple(listOf(2L, 3L, 10L))).isEqualTo(30)
        assertThat(Day8().leastCommonMultiple(listOf(2L, 3L, 4L))).isEqualTo(12)
        assertThat(Day8().leastCommonMultiple(listOf(2L, 2L, 4L))).isEqualTo(4)
        assertThat(Day8().leastCommonMultiple(listOf(4L, 4L, 10L))).isEqualTo(20)
    }
}

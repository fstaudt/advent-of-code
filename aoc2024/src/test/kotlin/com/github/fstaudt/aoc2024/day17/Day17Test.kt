package com.github.fstaudt.aoc2024.day17

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day17("example_day17.txt").part1AsString()).isEqualTo("4,6,3,5,6,3,5,2,1,0")
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day17().part1AsString()).isEqualTo("1,3,7,4,6,4,2,3,5")
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day17("example_day17_2.txt").part2()).isEqualTo(117440)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day17().part2()).isEqualTo(202367025818154)
    }
}
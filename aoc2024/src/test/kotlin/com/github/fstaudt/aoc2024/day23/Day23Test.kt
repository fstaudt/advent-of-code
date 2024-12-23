package com.github.fstaudt.aoc2024.day23

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day23Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day23("example_day23.txt").part1()).isEqualTo(7)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day23().part1()).isEqualTo(1173)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day23("example_day23.txt").part2AsString()).isEqualTo("co,de,ka,ta")
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day23().part2AsString()).isEqualTo("cm,de,ez,gv,hg,iy,or,pw,qu,rs,sn,uc,wq")
    }
}
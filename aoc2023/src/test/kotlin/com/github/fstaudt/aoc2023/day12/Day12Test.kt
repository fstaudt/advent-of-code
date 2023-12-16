package com.github.fstaudt.aoc2023.day12

import com.github.fstaudt.aoc2023.day12.Day12.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class Day12Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day12("example_day12.txt").part1()).isEqualTo(21)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day12().part1()).isEqualTo(8193)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day12("example_day12.txt").part2()).isEqualTo(525152)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day12().part2()).isEqualTo(45322533163795)
    }

    companion object {
        @JvmStatic
        fun arrangementsTestData() = listOf(
            of("", listOf(1), 0),
            of(".", listOf(1), 0),
            of("?", listOf(1), 1),
            of("#", listOf(1), 1),
            of(".?", listOf(1), 1),
            of("?.", listOf(1), 1),
            of(".#", listOf(1), 1),
            of("#.", listOf(1), 1),
            of("#?", listOf(1), 1),
            of("??", listOf(1), 2),
            of("???.###", listOf(1,1,3), 1),
            of(".??..??...?##.", listOf(1,1,3), 4),
            of("?#?#?#?#?#?#?#?", listOf(1,3,1,6), 1),
            of("????.#...#...", listOf(4,1,1), 1),
            of("????.######..#####.", listOf(1,6,5), 4),
            of("?###????????", listOf(3,2,1), 10),
        )
    }

    @ParameterizedTest
    @MethodSource("arrangementsTestData")
    fun `toArrangements should produce expected results`(springs: String, numbers: List<Int>, result: Long) {
        assertThat(Row(springs, numbers).toArrangements()).isEqualTo(result)
    }
}

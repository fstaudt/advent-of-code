package com.github.fstaudt.aoc2023.day7

import com.github.fstaudt.aoc2023.day7.Day7.Type.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day7Test {
    @Test
    fun `part 1 should produce expected result for example`() {
        assertThat(Day7("example_day7.txt").part1()).isEqualTo(6440)
    }

    @Test
    fun `part 1 should produce expected result for my input`() {
        assertThat(Day7().part1()).isEqualTo(253933213)
    }

    @Test
    fun `part 2 should produce expected result for example`() {
        assertThat(Day7("example_day7.txt").part2()).isEqualTo(5905)
    }

    @Test
    fun `part 2 should produce expected result for my input`() {
        assertThat(Day7().part2()).isEqualTo(253473930)
    }

    @Test
    fun `typeWithJokers should give expected output`() {
        assertThat(Day7().hand("23456 1").typeWithJokers()).isEqualTo(HIGH_CARD)
        assertThat(Day7().hand("23256 1").typeWithJokers()).isEqualTo(ONE_PAIR)
        assertThat(Day7().hand("23J56 1").typeWithJokers()).isEqualTo(ONE_PAIR)
        assertThat(Day7().hand("23236 1").typeWithJokers()).isEqualTo(TWO_PAIRS)
        assertThat(Day7().hand("23236 1").typeWithJokers()).isEqualTo(TWO_PAIRS)
        assertThat(Day7().hand("23226 1").typeWithJokers()).isEqualTo(THREE_OF_A_KIND)
        assertThat(Day7().hand("232J6 1").typeWithJokers()).isEqualTo(THREE_OF_A_KIND)
        assertThat(Day7().hand("23JJ6 1").typeWithJokers()).isEqualTo(THREE_OF_A_KIND)
        assertThat(Day7().hand("23232 1").typeWithJokers()).isEqualTo(FULL_HOUSE)
        assertThat(Day7().hand("23J32 1").typeWithJokers()).isEqualTo(FULL_HOUSE)
        assertThat(Day7().hand("22232 1").typeWithJokers()).isEqualTo(FOUR_OF_A_KIND)
        assertThat(Day7().hand("2223J 1").typeWithJokers()).isEqualTo(FOUR_OF_A_KIND)
        assertThat(Day7().hand("22J3J 1").typeWithJokers()).isEqualTo(FOUR_OF_A_KIND)
        assertThat(Day7().hand("2JJ3J 1").typeWithJokers()).isEqualTo(FOUR_OF_A_KIND)
        assertThat(Day7().hand("22222 1").typeWithJokers()).isEqualTo(FIVE_OF_A_KIND)
        assertThat(Day7().hand("2222J 1").typeWithJokers()).isEqualTo(FIVE_OF_A_KIND)
        assertThat(Day7().hand("222JJ 1").typeWithJokers()).isEqualTo(FIVE_OF_A_KIND)
        assertThat(Day7().hand("22JJJ 1").typeWithJokers()).isEqualTo(FIVE_OF_A_KIND)
        assertThat(Day7().hand("2JJJJ 1").typeWithJokers()).isEqualTo(FIVE_OF_A_KIND)
        assertThat(Day7().hand("JJJJJ 1").typeWithJokers()).isEqualTo(FIVE_OF_A_KIND)
    }
}

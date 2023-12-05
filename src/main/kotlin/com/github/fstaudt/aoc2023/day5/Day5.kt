package com.github.fstaudt.aoc2023.day5

import com.github.fstaudt.aoc2023.shared.LongDay
import com.github.fstaudt.aoc2023.shared.readInputLines
import kotlin.math.min

fun main() {
    Day5().run()
}


class Day5 : LongDay {

    override val input: List<String> = readInputLines(5)
    private val seedToSoilMappings = input.toMappings("seed-to-soil")
    private val soilToFertilizerMappings = input.toMappings("soil-to-fertilizer")
    private val fertilizerToWaterMappings = input.toMappings("fertilizer-to-water")
    private val waterToLightMappings = input.toMappings("water-to-light")
    private val lightToTemperatureMappings = input.toMappings("light-to-temperature")
    private val temperatureToHumidityMappings = input.toMappings("temperature-to-humidity")
    private val humidityToLocationMappings = input.toMappings("humidity-to-location")

    override fun part1() = input.lowestLocation()

    override fun part2() = input.lowestLocationWithRange()

    private fun List<String>.lowestLocation() = seeds().minOf { it.mapNumber() }

    private fun List<String>.lowestLocationWithRange(): Long {
        return get(0).split(":")[1].split(" ").filter { it.isNotBlank() }
                .chunked(2)
                .minOf { pairs ->
                    var min = Long.MAX_VALUE
                    for (it in pairs[0].toLong()..pairs[0].toLong() + pairs[1].toLong()) {
                        min = min(min, Seed(it).mapNumber())
                    }
                    min
                }
    }

    private fun List<String>.seeds(): List<Seed> {
        return get(0).split(":")[1].split(" ").filter { it.isNotBlank() }.map { Seed(it.toLong()) }
    }

    private fun List<String>.toMappings(sourceToDest: String): List<Mapping> {
        return this.takeLastWhile { it != "$sourceToDest map:" }.takeWhile { it.isNotBlank() }
                .map { it.split(" ").filter { it.isNotBlank() } }
                .map { Mapping(it[0].toLong(), it[1].toLong(), it[2].toLong()) }
    }

    private fun List<Mapping>.mapNumber(number: Long) = find { it.matches(number) }?.map(number) ?: number

    private fun Seed.mapNumber(): Long {
        return seedToSoilMappings.mapNumber(number)
                .also { soil -> soilToFertilizerMappings.mapNumber(soil) }
                .also { fertilizer -> fertilizerToWaterMappings.mapNumber(fertilizer) }
                .also { water -> waterToLightMappings.mapNumber(water) }
                .also { light -> lightToTemperatureMappings.mapNumber(light) }
                .also { temperature -> temperatureToHumidityMappings.mapNumber(temperature) }
                .also { humidity -> humidityToLocationMappings.mapNumber(humidity) }
    }

    data class Seed(var number: Long)
    data class Mapping(val destination: Long, val source: Long, val range: Long) {
        fun matches(number: Long): Boolean = source <= number && number < source + range
        fun map(number: Long) = number - source + destination
    }
}







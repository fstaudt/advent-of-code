package com.github.fstaudt.aoc2023.day20

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.LongExtensions.toLeastCommonMultiple
import com.github.fstaudt.aoc.shared.StringExtensions.toGroupValues
import com.github.fstaudt.aoc2023.day20.Day20.ModuleType.*
import com.github.fstaudt.aoc2023.day20.Day20.PulseType.HIGH
import com.github.fstaudt.aoc2023.day20.Day20.PulseType.LOW
import com.github.fstaudt.aoc2023.day20.Day20.State.OFF
import com.github.fstaudt.aoc2023.day20.Day20.State.ON

fun main() {
    Day20().run()
}

class Day20(fileName: String = "day_20.txt") : Day {
    override val input: List<String> = readInputLines(fileName)

    override fun part1() = countPulsesAfter1000Pushes()
    override fun part2() = countPushesUntilFinalGetLowPulse()

    private fun countPulsesAfter1000Pushes(): Long {
        val modules = input.toModules()
        var highCount = 0L
        var lowCount = 0L
        for (push in (1..1000)) {
            val pulses = MutableList(1) { Pulse("", "roadcaster", LOW) }
            while (pulses.isNotEmpty()) {
                val pulse = pulses.removeFirst()
                if (pulse.type == HIGH) highCount++ else lowCount++
                val module = modules[pulse.destination] ?: Module(pulse.destination, FINAL, emptyList())
                when (module.type) {
                    BROADCASTER ->
                        module.destinations.forEach {
                            pulses += Pulse(module.id, it, pulse.type)
                        }

                    FLIP_FLOP -> {
                        if (pulse.type == LOW) {
                            when (module.state) {
                                ON -> {
                                    module.state = OFF
                                    module.destinations.forEach {
                                        pulses += Pulse(module.id, it, LOW)
                                    }
                                }

                                OFF -> {
                                    module.state = ON
                                    module.destinations.forEach {
                                        pulses += Pulse(module.id, it, HIGH)
                                    }
                                }
                            }
                        }
                    }

                    CONJUNCTION -> {
                        module.inputs[pulse.origin] = pulse.type
                        module.state = if (module.inputs.all { it.value == HIGH }) ON else OFF
                        when (module.state) {
                            ON -> {
                                module.destinations.forEach {
                                    pulses += Pulse(module.id, it, LOW)
                                }
                            }

                            OFF -> {
                                module.destinations.forEach {
                                    pulses += Pulse(module.id, it, HIGH)
                                }
                            }
                        }
                    }

                    FINAL ->
                        if (pulse.type == LOW) module.state = ON
                }
            }
        }
        return highCount * lowCount
    }

    private fun countPushesUntilFinalGetLowPulse(): Long {
        val modules = input.toModules()
        var pushCount = 0L
        val lastConjunction = modules.values.first { it.type == CONJUNCTION && it.destinations.contains("rx") }
        val flipFlopGroups =
            modules.values.filter { it.type == CONJUNCTION && it.destinations.contains(lastConjunction.id) }
        while (!flipFlopGroups.all { it.pushCountBeforeFirstHighPulse > 0 }) {
            val pulses = MutableList(1) { Pulse("", "roadcaster", LOW) }
            pushCount++
            while (pulses.isNotEmpty()) {
                val pulse = pulses.removeFirst()
                val module = modules[pulse.destination] ?: Module(pulse.destination, FINAL, emptyList())
                when (module.type) {
                    FINAL ->
                        if (pulse.type == LOW) module.state = ON

                    BROADCASTER ->
                        module.destinations.forEach {
                            pulses += Pulse(module.id, it, pulse.type)
                        }

                    FLIP_FLOP -> {
                        if (pulse.type == LOW) {
                            when (module.state) {
                                ON -> {
                                    module.state = OFF
                                    module.destinations.forEach {
                                        pulses += Pulse(module.id, it, LOW)
                                    }
                                }

                                OFF -> {
                                    module.state = ON
                                    module.destinations.forEach {
                                        pulses += Pulse(module.id, it, HIGH)
                                    }
                                }
                            }
                        }
                    }

                    CONJUNCTION -> {
                        module.inputs[pulse.origin] = pulse.type
                        module.state = if (module.inputs.values.all { it == HIGH }) ON else OFF
                        when (module.state) {
                            ON -> {
                                module.destinations.forEach {
                                    pulses += Pulse(module.id, it, LOW)
                                }
                            }

                            OFF -> {
                                module.pushCountBeforeFirstHighPulse = pushCount
                                module.highPulses += pushCount
                                module.destinations.forEach {
                                    pulses += Pulse(module.id, it, HIGH)
                                }
                            }
                        }
                    }
                }
            }
            // println("$pushCount: ${modules.values.joinToString(" ") { "${it.id}: {${it.state}, ${it.inputs}}" }}")
        }
        return flipFlopGroups.map { it.pushCountBeforeFirstHighPulse }.toLeastCommonMultiple()
    }

    private fun List<String>.toModules(): Map<String, Module> {
        return (listOf(Module("rx", FINAL, emptyList())) + filter { it.isNotBlank() }.map { line ->
            line.toGroupValues("(.*) -> (.*)").let { groups ->
                Module(groups[1].drop(1), ModuleType.of(groups[1].first()), groups[2].split(',').map { it.trim() })
            }
        }).associateBy { it.id }.also { modules ->
            val conjunctions = modules.values.filter { it.type == CONJUNCTION }.map { it.id }
            modules.values.forEach { module ->
                module.destinations.forEach { destination ->
                    if (destination in conjunctions) {
                        modules[destination]?.let { it.inputs[module.id] = LOW }
                    }
                }
            }
        }
    }

    data class Module(
        val id: String,
        val type: ModuleType,
        val destinations: List<String>,
        val inputs: MutableMap<String, PulseType> = mutableMapOf(),
        var state: State = OFF,
        var pushCountBeforeFirstHighPulse: Long = 0,
        var highPulses: MutableList<Long> = mutableListOf(),
    )

    enum class ModuleType(val char: Char) {
        FLIP_FLOP('%'),
        CONJUNCTION('&'),
        BROADCASTER('b'),
        FINAL('#');

        companion object {
            fun of(char: Char) = entries.first { it.char == char }
        }
    }

    data class Pulse(val origin: String, val destination: String, val type: PulseType)
    enum class PulseType { HIGH, LOW }
    enum class State { ON, OFF }
}

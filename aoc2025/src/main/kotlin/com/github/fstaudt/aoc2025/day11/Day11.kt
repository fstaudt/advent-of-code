package com.github.fstaudt.aoc2025.day11

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import guru.nidi.graphviz.attribute.Attributes
import guru.nidi.graphviz.attribute.Color.BLACK
import guru.nidi.graphviz.attribute.Color.PINK
import guru.nidi.graphviz.attribute.ForNode
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT
import guru.nidi.graphviz.attribute.Style.FILLED
import guru.nidi.graphviz.engine.Format.SVG
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Factory.node
import java.io.File

fun main() {
    Day11().let {
        it.run()
        it.visualize()
    }
}

class Day11(fileName: String = "day_11.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val serverRack = ServerRack(input.map { line ->
        line.split(":").let { it[0] to Device(it[0], it[1].trim().split(" ")) }
    }.associate { it })

    override fun part1() = serverRack.countPathsFrom("you")

    override fun part2() = serverRack.countPathsPassingBy("dac", "fft") + serverRack.countPathsPassingBy("fft", "dac")

    fun visualize() = serverRack.visualizeIn(File("aoc2025/build/png/rack.svg"))

    private data class ServerRack(val devices: Map<String, Device>) {
        fun countPathsFrom(from: String, until: String = "out"): Long {
            return if (from == until) 1L
            else (devices[from]?.outputs?.sumOf { countPathsFrom(it) } ?: 0L)
        }

        fun countPathsPassingBy(firstCheckpoint: String, secondCheckpoint: String): Long {
            initInputs()
            val firstCheckpointRack = filteredUntil(firstCheckpoint)
            val secondCheckpointRack = filteredUntil(secondCheckpoint)
            secondCheckpointRack.countPathsFrom(firstCheckpoint, secondCheckpoint).let { dacCount ->
                if (dacCount == 0L) return 0L
                firstCheckpointRack.countPathsFrom("svr", firstCheckpoint).let { fftCount ->
                    if (fftCount == 0L) return 0L
                    countPathsFrom(secondCheckpoint).let { outCount ->
                        return fftCount * dacCount * outCount
                    }
                }
            }
        }

        private fun initInputs() {
            devices.values.forEach { device ->
                device.inputs = devices.filterValues { device.id in it.outputs }.map { it.key }
            }
        }

        private fun filteredUntil(until: String): ServerRack {
            devices.forEach { it.value.visited = false }
            devices.visitInputs(until)
            val inputs = devices.filterValues { it.visited }.map { it.key }
            return ServerRack(devices.filter { it.key in inputs })
        }

        private fun Map<String, Device>.visitInputs(until: String) {
            get(until)?.inputs?.mapNotNull { get(it) }?.filter { !it.visited }?.forEach {
                it.visited = true
                visitInputs(it.id)
            }
        }

        fun visualizeIn(file: File) {
            val visualization = graph("device")
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .with(node("out").with(*arrayOf(PINK, FILLED)))
                .with(devices.map { (key, device) ->
                    node(key).with(*device.color()).link(device.links())
                })
            Graphviz.fromGraph(visualization).totalMemory(1024000000).render(SVG).toFile(file)
        }
    }

    private data class Device(
        val id: String,
        val outputs: List<String>,
        var inputs: List<String> = emptyList(),
        var visited: Boolean = false
    ) {
        fun links() = outputs.map { node(it).linkTo() }
        fun color(): Array<Attributes<out ForNode>> {
            return if (id in listOf("svr", "dac", "fft")) arrayOf(PINK, FILLED) else arrayOf(BLACK)
        }
    }
}

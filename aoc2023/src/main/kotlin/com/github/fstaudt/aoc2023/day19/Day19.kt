package com.github.fstaudt.aoc2023.day19

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines
import com.github.fstaudt.aoc.shared.StringExtensions.toGroupValues

fun main() {
    Day19().run()
}

class Day19(fileName: String = "day_19.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    private val workflows = input.takeWhile { it.isNotEmpty() }.map { it.toGroupValues("([a-z]+)\\{(.*)}") }
        .map { workflow ->
            val rules = workflow[2].split(",").dropLast(1).map { rule ->
                rule.split(":").let {
                    Rule(Condition(it[0][0], it[0][1] == '>', it[0].drop(2).toLong()), it[1])
                }
            }
            Workflow(workflow[1], rules, workflow[2].split(",").last())
        }.associateBy { it.id }
    private val parts = input.dropWhile { it.isNotEmpty() }.drop(1).takeWhile { it.isNotEmpty() }.map { part ->
        val ratings = part.drop(1).dropLast(1).split(",")
            .map { it.split("=") }.associate { it[0][0] to it[1].toLong() }
        Part(ratings)
    }

    override fun part1() = sumRatingsOfAcceptedParts()

    override fun part2() = sumRatingsOfAcceptedPartsInRange()

    private fun sumRatingsOfAcceptedParts(): Long {
        return parts.sumOf { part ->
            var result = "in"
            while (result != "A" && result != "R") {
                result = workflows[result]!!.let { workflow ->
                    workflow.rules.firstOrNull { it.condition.matchesFor(part) }?.workflowOrResult
                        ?: workflow.defaultWorkflowOrResult
                }
            }
            if (result == "A") part.ratings.values.sum() else 0
        }
    }

    private fun sumRatingsOfAcceptedPartsInRange(): Long {
        var ranges = listOf(Range("in", "xmas".associate { it to Values(1, 4000) }))
        while (!ranges.all { it.workflowOrResult == "A" }) {
            ranges = ranges.flatMap { range ->
                when {
                    (range.workflowOrResult == "A") -> listOf(range)
                    (range.workflowOrResult == "R") -> emptyList()
                    else -> range.splitBy(workflows[range.workflowOrResult]!!)
                }
            }
        }
        return ranges.sumOf {
            val x = it.values['x']!!.run { max - min + 1 }
            val m = it.values['m']!!.run { max - min + 1 }
            val a = it.values['a']!!.run { max - min + 1 }
            val s = it.values['s']!!.run { max - min + 1 }
            x * m * a * s
        }
    }

    data class Workflow(val id: String, val rules: List<Rule>, val defaultWorkflowOrResult: String)
    data class Rule(val condition: Condition, val workflowOrResult: String)
    data class Condition(val rating: Char, val greater: Boolean, val value: Long) {
        fun matchesFor(part: Part): Boolean {
            return part.ratings[rating]!!.let { if (greater) it > value else it < value }
        }
    }

    data class Part(val ratings: Map<Char, Long>)

    data class Range(val workflowOrResult: String, val values: Map<Char, Values>) {
        override fun toString() =
            "$workflowOrResult: ${"xmas".map { "$it=[${values[it]!!.min},${values[it]!!.max}] " }}"

        fun splitBy(workflow: Workflow): List<Range> {
            if (workflow.rules.isEmpty()) return listOf(Range(workflow.defaultWorkflowOrResult, values))
            val rule = workflow.rules.first()
            val value = values[rule.condition.rating]!!
            val current = if (rule.condition.greater)
                Values(rule.condition.value + 1, value.max)
            else
                Values(value.min, rule.condition.value - 1)
            val rest = if (rule.condition.greater)
                Values(value.min, rule.condition.value)
            else
                Values(rule.condition.value, value.max)
            val currentValues = values.minus(rule.condition.rating).plus(rule.condition.rating to current)
            val restValues = values.minus(rule.condition.rating).plus(rule.condition.rating to rest)
            val smallerWorkflow = workflow.run { Workflow(id, rules.drop(1), defaultWorkflowOrResult) }
            return listOf(Range(rule.workflowOrResult, currentValues)) +
                    Range(rule.workflowOrResult, restValues).splitBy(smallerWorkflow)
        }
    }

    data class Values(val min: Long, val max: Long)
}

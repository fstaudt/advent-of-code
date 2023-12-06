package com.github.fstaudt.aoc2023.shared

fun readInputLines(fileName: String) = readInput(fileName).lines()
private fun readInput(fileName: String) = "/$fileName".run { resource()?.readText()?.trim() ?: error("Unable to read file: '$this'") }

private fun String.resource() = object {}::class.java.getResource(this)

package com.github.fstaudt.aoc

import org.gradle.api.provider.Property

interface AdventOfCodeExtension {

    val year: Property<Int>
}
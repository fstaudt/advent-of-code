package com.github.fstaudt.aoc.shared

object LongExtensions {
    fun List<Long>.toLeastCommonMultiple(): Long {
        return map { it.splitByFactors() }
            .reduce { fl1, fl2 -> fl1 + fl1.fold(fl2) { fl, f -> fl.minus(f) } }
            .reduce { a, b -> a * b }
    }

    private fun Long.splitByFactors(): List<Long> {
        val number = this
        var factor = 2L
        while (factor * factor <= number) {
            if ((number / factor) * factor == number) {
                return listOf(factor) + (number / factor).splitByFactors()
            }
            factor++
        }
        return listOf(number)
    }
}

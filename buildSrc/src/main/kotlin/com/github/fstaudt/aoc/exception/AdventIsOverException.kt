package com.github.fstaudt.aoc.exception

class AdventIsOverException(day: String) :
    Exception("There is only 25 days in Advent of Code, not $day. Go back to sleep!")

package com.github.fstaudt.aoc2024.day09

import com.github.fstaudt.aoc.shared.Day
import com.github.fstaudt.aoc.shared.Input.readInputLines

fun main() {
    Day09().run()
}

class Day09(fileName: String = "day_09.txt") : Day {
    override val input: List<String> = readInputLines(fileName)
    val files = input[0].chunked(2).mapIndexed { i, chars ->
        File(i, chars[0].digitToInt(), chars.getOrNull(1)?.digitToInt() ?: 0)
    }

    override fun part1() = compactFiles().toCheckSum().value
    override fun part2() = compactFullFilesOnce().toCheckSum().value

    private fun compactFiles(): List<File> {
        val compactedFiles = files.toMutableList()
        while (compactedFiles.dropLast(1).maxOf { it.spaces } > 0) {
            val movableFile = compactedFiles.last()
            val targetIndex = compactedFiles.indexOfFirst { it.spaces > 0 }
            val targetFile = compactedFiles[targetIndex]
            when {
                movableFile.size <= targetFile.spaces -> {
                    compactedFiles[compactedFiles.size - 2].spaces += movableFile.spaces
                    movableFile.spaces = targetFile.spaces - movableFile.size
                    targetFile.spaces = 0
                    compactedFiles.add(targetIndex + 1, movableFile)
                    compactedFiles.removeLast()
                }

                else -> {
                    val partOfMovableFile = File(movableFile.id, targetFile.spaces, 0)
                    movableFile.spaces += movableFile.size - targetFile.spaces
                    movableFile.size -= targetFile.spaces
                    targetFile.spaces = 0
                    compactedFiles.add(targetIndex + 1, partOfMovableFile)
                }
            }
        }
        return compactedFiles
    }

    private fun compactFullFilesOnce(): List<File> {
        val compactedFiles = files.toMutableList()
        for (movableId in (1..<files.size).reversed()) {
            val movableIndex = compactedFiles.indexOfFirst { it.id == movableId }
            val targetIndex = compactedFiles.indexOfFirst { it.spaces >= compactedFiles[movableIndex].size }
            if (targetIndex >= 0 && targetIndex < movableIndex) {
                val movableFile = compactedFiles[movableIndex]
                val originFile = compactedFiles[movableIndex - 1]
                val targetFile = compactedFiles[targetIndex]
                originFile.spaces += movableFile.size + movableFile.spaces
                movableFile.spaces = targetFile.spaces - movableFile.size
                targetFile.spaces = 0
                compactedFiles.removeAt(movableIndex)
                compactedFiles.add(targetIndex + 1, movableFile)
            }
        }
        return compactedFiles
    }

    private fun List<File>.toCheckSum() = fold(CheckSum()) { checkSum, file ->
        checkSum.also {
            while (file.size > 0) {
                checkSum.value += checkSum.position * file.id
                checkSum.position++
                file.size--
            }
            checkSum.position += file.spaces
        }
    }

    data class File(val id: Int, var size: Int, var spaces: Int)
    data class CheckSum(var position: Long = 0, var value: Long = 0)
}
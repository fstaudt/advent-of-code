package com.github.fstaudt.aoc.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters.None
import java.io.File

abstract class JsonMapper : BuildService<None> {
    private val objectMapper = ObjectMapper().also {
        it.registerModule(KotlinModule.Builder().build())
    }

    fun <T> readValue(file: File, valueType: Class<T>): T {
        return objectMapper.readValue(file, valueType)
    }
}

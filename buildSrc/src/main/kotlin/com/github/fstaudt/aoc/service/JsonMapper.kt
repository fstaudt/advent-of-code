package com.github.fstaudt.aoc.service

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters.None
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.io.File

abstract class JsonMapper : BuildService<None> {
  private val objectMapper = JsonMapper.builder()
    .addModule(KotlinModule.Builder().build())
    .build()

  fun <T> readValue(file: File, valueType: Class<T>): T {
    return objectMapper.readValue(file, valueType)
  }
}

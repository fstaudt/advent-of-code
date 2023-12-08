package com.github.fstaudt.aoc.service

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import java.io.File

internal fun input(day: String, year: String, sessionCookieFile: File): String {
    val cookie = sessionCookieFile.readLines().first { it.isNotBlank() }
    return HttpClientBuilder.create().setDefaultHeaders(listOf(BasicHeader("Cookie", cookie))).build()
        .execute(HttpGet("https://adventofcode.com/$year/day/$day/input"))
        .entity.content.readAllBytes().let {
            String(it)
        }
}

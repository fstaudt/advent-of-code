group = "com.github.fstaudt"
version = "0.1.0"

plugins {
    id("buildSrc.aoc")
}

val gradleWrapperVersion: String by project
tasks.wrapper {
    gradleVersion = gradleWrapperVersion
}
plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("buildSrc.aoc")
}

group = "com.github.fstaudt"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

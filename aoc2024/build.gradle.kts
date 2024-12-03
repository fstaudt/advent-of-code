plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("buildSrc.aoc")
}

repositories {
    mavenCentral()
}

val junitVersion: String by project
val assertjVersion: String by project
dependencies {
    implementation(project(":shared"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}

adventOfCode {
    year = 2024
}

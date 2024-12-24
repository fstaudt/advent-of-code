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
    implementation("tools.aqua:z3-turnkey:4.13.0.1")
    // graphviz
    implementation("guru.nidi:graphviz-java:0.18.1")
    runtimeOnly("com.eclipsesource.j2v8:j2v8_win32_x86_64:4.6.0")

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

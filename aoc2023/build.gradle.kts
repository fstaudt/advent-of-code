plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("buildSrc.aoc")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.initDay {
    year = "2023"
}

tasks.fetchDayInput {
    year = "2023"
}

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("buildSrc.aoc")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("tools.aqua:z3-turnkey:4.12.2.1")
    // graphviz
    implementation("guru.nidi:graphviz-java:0.18.1")
    runtimeOnly("com.eclipsesource.j2v8:j2v8_win32_x86_64:4.6.0")
}

testing {
  suites {
    @Suppress("UnstableApiUsage")
    named<JvmTestSuite>("test") {
      useJUnitJupiter()
      val junitVersion: String by project
      val assertjVersion: String by project
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
        implementation("org.assertj:assertj-core:${assertjVersion}")
        implementation("org.junit.jupiter:junit-jupiter-params:${junitVersion}")
        runtimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")      }
    }
  }
}

adventOfCode {
    year = 2023
}

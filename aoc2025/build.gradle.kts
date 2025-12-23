plugins {
  kotlin("jvm") version embeddedKotlinVersion
  id("buildSrc.aoc")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":shared"))
  implementation("tools.aqua:z3-turnkey:4.14.1")
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
        runtimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
      }
    }
  }
}

adventOfCode {
  year = 2025
}

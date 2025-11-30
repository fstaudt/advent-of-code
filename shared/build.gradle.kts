plugins {
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
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

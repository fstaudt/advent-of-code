repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    `java-gradle-plugin`
}

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.14") {
        exclude("commons-codec", "commons-codec")
    }
    implementation("org.knowm.xchart:xchart:3.8.8")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
}

gradlePlugin {
    plugins {
        create("aoc") {
            id = "buildSrc.aoc"
            implementationClass = "com.github.fstaudt.aoc.AdventOfCodePlugin"
        }
    }
}

testing {
  suites {
    @Suppress("UnstableApiUsage")
    named<JvmTestSuite>("test") {
      useJUnitJupiter()
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
        implementation("org.assertj:assertj-core:3.27.6")
        runtimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.1")
      }
    }
  }
}

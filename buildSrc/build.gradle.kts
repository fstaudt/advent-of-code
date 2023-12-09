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
    implementation("org.knowm.xchart:xchart:3.8.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
}


gradlePlugin {
    plugins {
        create("aoc") {
            id = "buildSrc.aoc"
            implementationClass = "com.github.fstaudt.aoc.AdventOfCodePlugin"
        }
    }
}

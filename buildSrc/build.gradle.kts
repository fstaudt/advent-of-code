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
}


gradlePlugin {
    plugins {
        create("aoc") {
            id = "buildSrc.aoc"
            implementationClass = "com.github.fstaudt.aoc.AdventOfCodePlugin"
        }
    }
}

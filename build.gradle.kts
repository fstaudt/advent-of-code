plugins {
    kotlin("jvm") version embeddedKotlinVersion
}

group = "com.github.fstaudt"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

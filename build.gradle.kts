plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}

sourceSets.main {
    java.srcDirs("src")
}

tasks.wrapper {
    gradleVersion = "7.6"
}

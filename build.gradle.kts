buildscript {
    dependencies {
        classpath("io.ratpack:ratpack-gradle:1.9.0-rc-2")
    }
}

plugins {
    kotlin("jvm") version "1.5.0"
    groovy
}

apply {
    plugin("io.ratpack.ratpack-java")
}

group = "me.kcybulski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":domain"))
    implementation(project(":coinpaprika"))
    implementation(project(":redis"))
    implementation(project(":api"))
}

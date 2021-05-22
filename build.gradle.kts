buildscript {
    dependencies {
        classpath("io.ratpack:ratpack-gradle:1.9.0-rc-2")
    }
}

plugins {
    kotlin("jvm") version "1.5.0"
    groovy
    application
}

apply {
    plugin("io.ratpack.ratpack-java")
}

application {
    mainClass.set("me.kcybulski.smartsavings.StartKt")
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

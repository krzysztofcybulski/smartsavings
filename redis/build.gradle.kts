plugins {
    kotlin("jvm") version "1.5.0"
}

group = "me.kcybulski"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(kotlin("stdlib"))
    api("org.redisson:redisson:3.15.5")
}

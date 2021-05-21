plugins {
    kotlin("jvm") version "1.5.0"
    groovy
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

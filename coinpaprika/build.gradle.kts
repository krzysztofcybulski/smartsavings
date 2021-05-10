plugins {
    kotlin("jvm") version "1.5.0"
}

group = "me.kcybulski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(kotlin("stdlib"))
//    implementation("io.projectreactor.netty:reactor-netty-core:1.0.6")
    implementation("io.projectreactor.netty:reactor-netty-http:1.0.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.+")
}

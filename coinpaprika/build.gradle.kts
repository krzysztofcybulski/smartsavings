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
    implementation(project(":domain"))
    implementation(kotlin("stdlib"))
    implementation("io.projectreactor.netty:reactor-netty-http:1.0.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.+")

    testImplementation("org.codehaus.groovy:groovy-all:3.0.7")
    testImplementation("org.spockframework:spock-core:2.0-M5-groovy-3.0")
    testImplementation("com.github.tomakehurst:wiremock:2.27.2")
    testImplementation("io.projectreactor:reactor-test:3.4.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

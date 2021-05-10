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
    implementation(project(":coinpaprika"))
    implementation(kotlin("stdlib"))

    implementation("io.ratpack:ratpack-core:1.9.0-rc-2")
    implementation("io.ratpack:ratpack-reactor:1.9.0-rc-2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.+")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha1")

    testImplementation("org.codehaus.groovy:groovy-all:3.0.7")
    testImplementation("org.spockframework:spock-core:2.0-M5-groovy-3.0")
    testImplementation("io.ratpack:ratpack-groovy-test:1.9.0-rc-2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

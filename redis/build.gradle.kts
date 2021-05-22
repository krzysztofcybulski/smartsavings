plugins {
    kotlin("jvm")
    groovy
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

    testImplementation("org.codehaus.groovy:groovy-all:3.0.7")
    testImplementation("org.spockframework:spock-core:2.0-M5-groovy-3.0")
    testImplementation("org.testcontainers:spock:1.15.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

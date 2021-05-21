plugins {
    kotlin("jvm")
    groovy
    application
}

group = "me.kcybulski"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("me.kcybulski.smartsavings.StartKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api("io.projectreactor:reactor-core:3.4.5")

    testImplementation("org.codehaus.groovy:groovy-all:3.0.7")
    testImplementation("org.spockframework:spock-core:2.0-M5-groovy-3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

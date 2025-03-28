plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.dokka") version "1.9.10"
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.commons:commons-lang3:3.12.0")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("mad")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.dokkaHtml {
    outputDirectory.set(file("$projectDir/docs"))
}

kotlin {
    jvmToolchain(23)
}
plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.7.0")
    testImplementation("io.rest-assured:rest-assured:4.5.1")
    testImplementation("io.qameta.allure:allure-testng:2.17.3")
    testImplementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

tasks.test {
    useTestNG()
}
kotlin {
    jvmToolchain(19)
}

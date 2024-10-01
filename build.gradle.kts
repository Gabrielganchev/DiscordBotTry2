import org.springframework.boot.gradle.tasks.run.BootRun
import java.util.*

plugins {
    `java-library`
    `maven-publish`
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter)
    api(libs.org.springframework.boot.spring.boot.starter.webflux)
    api(libs.org.springframework.boot.spring.boot.starter.actuator)
    api(libs.com.discord4j.discord4j.core)
    api(libs.io.projectreactor.reactor.core)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "DiscordBotTry2"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

val privateProperties = Properties().apply {
    val propertiesFile = rootProject.file("private.properties")
    if (propertiesFile.exists()) {
        load(propertiesFile.inputStream())
    }
}

val token: String? = privateProperties.getProperty("TOKEN")

tasks.withType<BootRun> {
    if (token != null) {
        environment("TOKEN", token)
    }
}
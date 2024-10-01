pluginManagement {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
        maven("https://mvnrepository.com/repos/springio-plugins-release")
    }
    plugins {
        id("org.springframework.boot").version("3.3.4")
        id("io.spring.dependency-management").version("1.1.6")
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DiscordBotTry2"

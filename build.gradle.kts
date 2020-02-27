// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val kotlinVersion: String by extra("1.3.50")
    val gsonVersion: String by extra("2.8.6")
    val eToolsVersion: String by extra("v0.1-beta1.1")
    val junitVersion: String by extra("5.6.0")

    repositories {
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by rootProject.extra
val gsonVersion: String by rootProject.extra
val eToolsVersion: String by rootProject.extra
val junitVersion: String by rootProject.extra

plugins {
    kotlin("jvm")
}
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.github.OPEN-EDGN.ETOOL4K:tools4k:$eToolsVersion")
    implementation("com.github.OPEN-EDGN.ETOOL4K:security4k:$eToolsVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}
tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

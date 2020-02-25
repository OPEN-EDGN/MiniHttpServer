val kotlinVersion: String by rootProject.extra
val gsonVersion: String by rootProject.extra
val eToolsVer: String by rootProject.extra


plugins {
    kotlin("jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
    implementation("com.google.code.gson:gson:${gsonVersion}")
    implementation("com.github.OPEN-EDGN.ETOOL4K:tools4k:${eToolsVer}")
    implementation("com.github.OPEN-EDGN.ETOOL4K:security4k:${eToolsVer}")

    testImplementation("junit:junit:4.12")
}

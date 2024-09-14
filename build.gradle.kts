plugins {
    id("java")
}

group = "me.miguelbuccat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jmonkeyengine:jme3-core:3.3.2-stable")
    implementation("org.jmonkeyengine:jme3-desktop:3.3.2-stable")
    implementation("org.jmonkeyengine:jme3-lwjgl3:3.3.2-stable")
}

tasks.test {
    useJUnitPlatform()
}
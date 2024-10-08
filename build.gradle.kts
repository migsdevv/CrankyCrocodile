plugins {
    id("java")
}

group = "me.miguelbuccat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jmonkeyengine:jme3-core:3.3.2-stable")
    implementation("org.jmonkeyengine:jme3-desktop:3.3.2-stable")
    implementation("org.jmonkeyengine:jme3-lwjgl3:3.3.2-stable")
    implementation("org.jmonkeyengine:jme3-jogg:3.6.1-stable")
    implementation("org.jmonkeyengine:jme3-effects:3.6.1-stable")
    implementation("org.jmonkeyengine:jme3-plugins:3.6.1-stable")

    implementation("com.simsilica:lemur:1.16.0")
    implementation("com.simsilica:lemur-proto:1.13.0")

    implementation("org.apache.groovy:groovy-all:4.0.23")
    
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "me.miguelbuccat.Main"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
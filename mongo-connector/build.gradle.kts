plugins {
    id("net.codingarea.challenges.java-conventions")
    id("org.jetbrains.intellij") version "1.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation("org.mongodb:mongodb-driver:3.12.7")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation(project(":plugin"))
    implementation("net.anweisen.utilities:database-mongodb:1.3.0")
    compileOnly("net.anweisen.utilities:bukkit-utils:1.3.0")
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
}

group = "net.codingarea.challenges"
version = "2.0"
description = "mongo-connector"

tasks {
    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        project.shadow.component(this)
        repositories {
            maven("https://maven.pkg.github.com/anweisen/Challenges") {
                credentials {
                    username = extra["github.username"] as String
                    password = extra["github.token"] as String
                }
            }
        }
    }
}
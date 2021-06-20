plugins {
    id("net.codingarea.challenges.java-conventions")
    id("org.jetbrains.intellij") version "1.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

val utilitiesVersion = project.property("utilitiesVersion")
val cloudnetVersion = project.property("cloudnetVersion")

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("net.anweisen.utilities:bukkit-utils:$utilitiesVersion")
    implementation("net.anweisen.utilities:database-api:$utilitiesVersion")
    implementation("net.anweisen.utilities:database-sql:$utilitiesVersion")
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    compileOnly("de.dytanic.cloudnet:cloudnet-driver:$cloudnetVersion")
    compileOnly("de.dytanic.cloudnet:cloudnet-bridge:$cloudnetVersion")
}

group = "net.codingarea.challenges"
version = "2.0"
description = "plugin"

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

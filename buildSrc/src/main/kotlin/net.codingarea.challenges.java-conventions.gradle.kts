plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.cloudnetservice.eu/repository/releases/")
    }

    maven {
        url = uri("https://libraries.minecraft.net/")
    }

    maven {
        url = uri("https://maven.pkg.github.com/anweisen/Utilities")
        credentials {
            username = extra["github.username"] as String
            password = extra["github.token"] as String
        }
    }

    maven {
        url = uri("https://maven.pkg.github.com/anweisen/Challenges")
        credentials {
            username = extra["github.username"] as String
            password = extra["github.token"] as String
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


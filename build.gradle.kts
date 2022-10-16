@file:Suppress("UnstableApiUsage")

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "me.denarydev"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("http://repo.denaryworld.ru/snapshots/") { isAllowInsecureProtocol = true }
}

val bentoboxVersion = "1.21.1"

val plugin: Configuration by configurations.creating
configurations.compileOnly.get().extendsFrom(plugin)

dependencies {
    compileOnly("io.sapphiremc.sapphire:sapphire-api:1.19.2-R0.1-SNAPSHOT")

    plugin("world.bentobox:bentobox:$bentoboxVersion") { isTransitive = false }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    processResources {
        inputs.properties("version" to project.version, "bentobox_version" to bentoboxVersion)

        filesMatching(listOf("plugin.yml", "addon.yml")) {
            expand("version" to project.version, "bentobox_version" to bentoboxVersion)
        }
    }

    runServer {
        minecraftVersion("1.19.2")
        runDirectory.set(project.projectDir.resolve("run/"))
        plugin.files.forEach {
            pluginJars(it)
        }
        if (!System.getenv("useCustomCore").isNullOrEmpty()) {
            jvmArgs("--add-modules=jdk.incubator.vector")
            serverJar.set(project.projectDir.resolve("run/server.jar"))
        }
        doFirst {
            val jarName = "${rootProject.name}-${project.version}.jar"
            val target = project.projectDir.resolve("run/plugins/BentoBox/addons/$jarName")
            if (target.exists()) target.delete()
            project.projectDir.resolve("build/libs/$jarName").copyTo(target)
        }
    }
}
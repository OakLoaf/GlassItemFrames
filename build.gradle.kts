plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
}

group = "me.dave"
version = "1.2.2"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.lushplugins.org/snapshots")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.68.Final")
    implementation("fr.skytasul:glowingentities:1.3.5")
    implementation("org.lushplugins:ChatColorHandler:3.1.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("fr.skytasul", "me.dave.glassitemframes.libraries.skytasul")
        relocate("org.lushplugins.chatcolorhandler", "me.dave.glassitemframes.libraries.chatcolor")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        expand(project.properties)

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }
}
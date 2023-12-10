plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "1.2.2"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://jitpack.io")}
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.68.Final")
    implementation("io.github.skytasul:glowingentities:1.3.1")
    implementation("com.github.CoolDCB:ChatColorHandler:v1.2.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("fr.skytasul", "me.dave.glassitemframes.libraries.skytasul")
        relocate("me.dave.chatcolorhandler", "me.dave.glassitemframes.libraries.chatcolor")

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
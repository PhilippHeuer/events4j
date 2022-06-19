plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.dokka") version "1.7.0"
}

dependencies {
    // Project
    api(project(":api"))
    api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.6.2")

    // Testing
    testImplementation(group = "org.jetbrains.kotlinx", name="kotlinx-coroutines-test", version = "1.6.2")
    testImplementation(project(":core"))
    testImplementation(project(":handler-simple"))
}

tasks.test {
    useJUnitPlatform {
        includeTags("unittest")
        excludeTags("integration")
    }
}

tasks.javadoc {
    enabled = false
}

tasks.javadocJar {
    from(tasks.dokkaJavadoc)
}

tasks.dokkaJavadoc {
    moduleName.set("Events4J (v${version}) - Kotlin extension functions")

    dokkaSourceSets {
        configureEach {
            jdkVersion.set(8)

            sourceLink {
                localDirectory.set(file("src/main/java"))
                remoteUrl.set(uri("https://github.com/PhilippHeuer/events4j/tree/master/kotlin/src/main/java").toURL())
                remoteLineSuffix.set("#L")
            }

            externalDocumentationLink {
                url.set(uri("https://github.com/PhilippHeuer/events4j").toURL())
            }
        }
    }
}

publishing.publications.withType<MavenPublication> {
    artifactId = "events4j-kotlin"
    pom {
        name.set("Events4J - Kotlin")
        description.set("Events4J - Kotlin extension functions")
    }
}

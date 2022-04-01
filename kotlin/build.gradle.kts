plugins {
    kotlin("jvm") version "1.6.20"
}

dependencies {
    // Project
    api(project(":api"))
    api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.6.0")

    // Testing
    testImplementation(group = "org.jetbrains.kotlinx", name="kotlinx-coroutines-test", version = "1.6.0")
    testImplementation(project(":core"))
    testImplementation(project(":handler-simple"))
}

tasks.test {
    useJUnitPlatform {
        includeTags("unittest")
        excludeTags("integration")
    }
}

publishing.publications.withType<MavenPublication> {
    artifactId = "events4j-kotlin"
    pom {
        name.set("Events4J - Kotlin")
        description.set("Events4J - Kotlin extension functions")
    }
}

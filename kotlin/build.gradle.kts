plugins {
    kotlin("jvm")
}

projectConfiguration {
    language.set(me.philippheuer.projectcfg.domain.ProjectLanguage.KOTLIN)
    artifactId.set("events4j-kotlin")
    artifactDisplayName.set("Events4J - Kotlin")
    artifactDescription.set("Events4J - Kotlin extension functions")
}

dependencies {
    // Project
    api(project(":api"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation(project(":core"))
    testImplementation(project(":handler-simple"))
}

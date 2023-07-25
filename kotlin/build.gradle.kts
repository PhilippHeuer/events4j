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
    api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.7.3")

    // Testing
    testImplementation(group = "org.jetbrains.kotlinx", name="kotlinx-coroutines-test", version = "1.7.3")
    testImplementation(project(":core"))
    testImplementation(project(":handler-simple"))
}

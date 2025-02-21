plugins {
    kotlin("jvm")
}

projectConfiguration {
    language.set(me.philippheuer.projectcfg.domain.ProjectLanguage.KOTLIN)
    artifactId.set("events4j-kotlin")
    artifactDisplayName.set("Events4J - Kotlin")
    artifactDescription.set("Events4J - Kotlin extension functions")
    type.set(me.philippheuer.projectcfg.domain.ProjectType.LIBRARY)
}

dependencies {
    // project
    api(project(":api"))
    api(libs.kotlinCoroutinesCore)

    // testing
    testImplementation(libs.kotlinCoroutinesTest)
    testImplementation(project(":core"))
    testImplementation(project(":handler-simple"))
}

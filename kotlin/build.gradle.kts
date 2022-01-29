// In this section you declare the dependencies for your production and test code
dependencies {
    val versionKotlin = "1.6.0"

    // Project
    compileOnly(project(":core"))

    // Kotlin dependencies
    api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = versionKotlin)

    // Tests
    testImplementation(project(":core"))
    testImplementation(project(":handler-simple"))
    testImplementation(testFixtures(project(":core")))
    testImplementation(group = "org.jetbrains.kotlinx", name="kotlinx-coroutines-test", version = versionKotlin)
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

plugins {
    `java-library`
}

projectConfiguration {
    artifactId.set("events4j-api")
    artifactDisplayName.set("Events4J API")
    artifactDescription.set("Events4J API")
}

dependencies {
    // annotations
    implementation(libs.jspecify)
}

tasks.withType<Jar> {
    manifest {
        attributes("Automatic-Module-Name" to "events4j.api")
    }
}

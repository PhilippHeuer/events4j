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
    implementation("org.jspecify:jspecify:1.0.0")
}

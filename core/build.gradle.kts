plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("events4j-core")
	artifactDisplayName.set("Events4J Core")
	artifactDescription.set("Events4J Core")
}

dependencies {
	// project
	api(project(":api"))
	testImplementation(project(":handler-simple"))
	testImplementation(project(":handler-reactor"))

	// annotations
	implementation(libs.jspecify)
}

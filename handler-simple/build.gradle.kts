plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("events4j-handler-simple")
	artifactDisplayName.set("Events4J Handler - Simple")
	artifactDescription.set("Events4J Handler - Simple")
}

dependencies {
	// project
	api(project(":api"))
	testImplementation(project(":core"))

	// annotations
	implementation(libs.jspecify)
}

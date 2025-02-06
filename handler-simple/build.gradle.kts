plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("events4j-handler-simple")
	artifactDisplayName.set("Events4J Handler - Simple")
	artifactDescription.set("Events4J Handler - Simple")
}

dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))
}

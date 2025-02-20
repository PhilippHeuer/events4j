plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("events4j-handler-spring")
	artifactDisplayName.set("Events4J Handler - Spring")
	artifactDescription.set("Events4J Handler - Spring")
}

dependencies {
	// project
	api(project(":api"))
	testImplementation(project(":core"))

	// spring
	api(libs.springBootStarter)
	testImplementation(libs.springBootStarterTest)

	// annotations
	implementation(libs.jspecify)
}

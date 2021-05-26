// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":handler-simple"))
	testImplementation(project(":handler-reactor"))
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-core"
	pom {
		name.set("Events4J Core Module")
		description.set("Core Module")
	}
}

// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":events4j-api"))
	testImplementation(project(":events4j-handler-simple"))
	testImplementation(project(":events4j-handler-reactor"))
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-core"
	pom {
		name.set("Events4J Core Module")
		description.set("Core Module")
	}
}

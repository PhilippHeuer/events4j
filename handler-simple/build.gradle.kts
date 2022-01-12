// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))
	testImplementation(testFixtures(project(":core")))
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-simple"
	pom {
		name.set("Events4J Handler - Simple")
		description.set("Events4J Handler - Simple")
	}
}

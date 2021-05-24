// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":events4j-api"))
	testImplementation(project(":events4j-core"))
	testImplementation(testFixtures(project(":events4j-core")))
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-simple"
	pom {
		name.set("Events4J Handler - Simple")
		description.set("Events4J Handler - Simple")
	}
}

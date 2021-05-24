// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":events4j-api"))
	testImplementation(project(":events4j-core"))
	testImplementation(testFixtures(project(":events4j-core")))

	// Reactor
	api(group = "io.projectreactor", name = "reactor-core")
	api(group = "io.projectreactor.addons", name = "reactor-extra")
	testImplementation(group = "io.projectreactor", name = "reactor-test")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-reactor"
	pom {
		name.set("Events4J Handler - Reactor")
		description.set("Events4J Handler - Reactor")
	}
}

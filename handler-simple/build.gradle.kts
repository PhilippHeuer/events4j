// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))
	testImplementation(testFixtures(project(":core")))

	// class utils
	implementation("org.apache.commons:commons-lang3:3.12.0")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-simple"
	pom {
		name.set("Events4J Handler - Simple")
		description.set("Events4J Handler - Simple")
	}
}

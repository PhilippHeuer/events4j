dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-simple"
	pom {
		name.set("Events4J Handler - Simple")
		description.set("Events4J Handler - Simple")
	}
}

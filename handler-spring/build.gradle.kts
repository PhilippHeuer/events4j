// In this section you declare the dependencies for your production and test code
dependencies {
	// Project
	api(project(":events4j-api"))
	testImplementation(project(":events4j-core"))
	testImplementation(testFixtures(project(":events4j-core")))

	// Spring
	implementation(group = "org.springframework.boot", name = "spring-boot-starter", version = "2.4.5")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-spring"
	pom {
		name.set("Events4J Handler - Spring")
		description.set("Events4J Handler - Spring")
	}
}

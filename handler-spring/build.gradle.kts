dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))

	// Spring
	implementation(group = "org.springframework.boot", name = "spring-boot-starter", version = "2.6.4")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-spring"
	pom {
		name.set("Events4J Handler - Spring")
		description.set("Events4J Handler - Spring")
	}
}

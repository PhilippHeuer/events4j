projectConfiguration {
	artifactId.set("events4j-handler-spring")
	artifactDisplayName.set("Events4J Handler - Spring")
	artifactDescription.set("Events4J Handler - Spring")
}

dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))

	// Spring
	api("org.springframework.boot:spring-boot-starter:2.7.11")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.11")
}

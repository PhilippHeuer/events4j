plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("events4j-handler-spring")
	artifactDisplayName.set("Events4J Handler - Spring")
	artifactDescription.set("Events4J Handler - Spring")
}

dependencies {
	// project
	api(project(":api"))
	testImplementation(project(":core"))

	// spring
	api("org.springframework.boot:spring-boot-starter:2.7.18")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.18")

	// annotations
	implementation("org.jspecify:jspecify:1.0.0")
}

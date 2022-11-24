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
	implementation(group = "org.springframework.boot", name = "spring-boot-starter", version = "2.7.6")
}

plugins {
	`java-library`
}

projectConfiguration {
	artifactId.set("events4j-handler-reactor")
	artifactDisplayName.set("Events4J Handler - Reactor")
	artifactDescription.set("Events4J Handler - Reactor")
}

dependencies {
	// project
	api(project(":api"))
	testImplementation(project(":core"))

	// reactor - see https://repo1.maven.org/maven2/io/projectreactor/reactor-bom/Dysprosium-SR12/reactor-bom-Dysprosium-SR12.pom
	api("io.projectreactor:reactor-core:3.7.3")
	api("io.projectreactor.addons:reactor-extra:3.5.2")
	testImplementation("io.projectreactor:reactor-test:3.7.3")

	// annotations
	implementation("org.jspecify:jspecify:1.0.0")
}

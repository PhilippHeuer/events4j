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
	api(libs.reactorCore)
	api(libs.reactorExtra)
	testImplementation(libs.reactorTest)

	// annotations
	implementation(libs.jspecify)
}

tasks.withType<Jar> {
	manifest {
		attributes("Automatic-Module-Name" to "events4j.handler.reactor")
	}
}

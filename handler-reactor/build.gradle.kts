projectConfiguration {
	artifactId.set("events4j-handler-reactor")
	artifactDisplayName.set("Events4J Handler - Reactor")
	artifactDescription.set("Events4J Handler - Reactor")
}

dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))

	// Reactor - see https://repo1.maven.org/maven2/io/projectreactor/reactor-bom/Dysprosium-SR12/reactor-bom-Dysprosium-SR12.pom
	api("io.projectreactor:reactor-core:3.7.1")
	api("io.projectreactor.addons:reactor-extra:3.5.2")
	testImplementation("io.projectreactor:reactor-test:3.7.1")
}

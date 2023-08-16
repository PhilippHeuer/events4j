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
	api(group = "io.projectreactor", name = "reactor-core", version = "3.5.9")
	api(group = "io.projectreactor.addons", name = "reactor-extra", version = "3.5.1")
	testImplementation(group = "io.projectreactor", name = "reactor-test", version = "3.5.9")
}

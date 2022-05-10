dependencies {
	// Project
	api(project(":api"))
	testImplementation(project(":core"))

	// Reactor - see https://repo1.maven.org/maven2/io/projectreactor/reactor-bom/Dysprosium-SR12/reactor-bom-Dysprosium-SR12.pom
	api(group = "io.projectreactor", name = "reactor-core", version = "3.4.17")
	api(group = "io.projectreactor.addons", name = "reactor-extra", version = "3.4.8")
	testImplementation(group = "io.projectreactor", name = "reactor-test", version = "3.4.18")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-handler-reactor"
	pom {
		name.set("Events4J Handler - Reactor")
		description.set("Events4J Handler - Reactor")
	}
}

// In this section you declare the dependencies for your production and test code
dependencies {

}

publishing.publications.withType<MavenPublication> {
	artifactId = "events4j-api"
	pom {
		name.set("Events4J API Module")
		description.set("API Module")
	}
}

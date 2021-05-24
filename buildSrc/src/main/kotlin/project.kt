import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloperSpec

fun MavenPom.default() {
	url.set("https://github.com/PhilippHeuer/events4j")
	issueManagement {
		system.set("GitHub")
		url.set("https://github.com/PhilippHeuer/events4j/issues")
	}
	inceptionYear.set("2019")
	developers { all }
	licenses {
		license {
			name.set("MIT Licence")
			distribution.set("repo")
			url.set("https://opensource.org/licenses/MIT")
		}
	}
	scm {
		connection.set("scm:git:https://github.com/PhilippHeuer/events4j.git")
		developerConnection.set("scm:git:git@github.com:PhilippHeuer/events4j.git")
		url.set("https://github.com/PhilippHeuer/events4j")
	}
}

val MavenPomDeveloperSpec.all: Unit
	get() {
		PhilippHeuer()
	}

fun MavenPomDeveloperSpec.PhilippHeuer() {
	developer {
		id.set("PhilippHeuer")
		name.set("Philipp Heuer")
		email.set("git@philippheuer.me")
		roles.addAll("maintainer")
	}
}

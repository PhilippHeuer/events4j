// Plugins
plugins {
    id("me.philippheuer.configuration") version "0.7.1"
}

version = properties["version"] as String

// All-Projects
allprojects {
    apply(plugin = "me.philippheuer.configuration")

    // Repositories
    repositories {
        mavenCentral()
    }

    projectConfiguration {
        language.set(me.philippheuer.projectcfg.domain.ProjectLanguage.JAVA)
        type.set(me.philippheuer.projectcfg.domain.ProjectType.LIBRARY)
        javaVersion.set(JavaVersion.VERSION_1_8)
        lombokVersion.set("1.18.24")
        artifactGroupId.set("com.github.philippheuer.events4j")

        pom = { pom ->
            pom.url.set("https://github.com/PhilippHeuer/events4j")
            pom.issueManagement {
                system.set("GitHub")
                url.set("https://github.com/PhilippHeuer/events4j/issues")
            }
            pom.inceptionYear.set("2018")
            pom.developers {
                developer {
                    id.set("PhilippHeuer")
                    name.set("Philipp Heuer")
                    email.set("git@philippheuer.me")
                    roles.addAll("maintainer")
                }
                developer {
                    id.set("iProdigy")
                    name.set("Sidd")
                    roles.addAll("maintainer")
                }
            }
            pom.licenses {
                license {
                    name.set("MIT Licence")
                    distribution.set("repo")
                    url.set("https://github.com/PhilippHeuer/events4j/blob/main/LICENSE")
                }
            }
            pom.scm {
                connection.set("scm:git:https://github.com/PhilippHeuer/events4j.git")
                developerConnection.set("scm:git:git@github.com:PhilippHeuer/events4j.git")
                url.set("https://github.com/PhilippHeuer/events4j")
            }
        }
    }
}

// Subprojects
subprojects {
    if (!name.contains("bom")) {
        apply(plugin = "java-library")
    }
}

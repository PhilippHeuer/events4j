// Plugins
plugins {
    signing
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "6.5.0.2"
}

group = group
version = version

// All-Projects
allprojects {
    // Repositories
    repositories {
        mavenCentral()
    }
}

// Subprojects
subprojects {
    apply(plugin = "signing")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    base {
        archivesBaseName = artifactId
    }

    if (!project.name.contains("kotlin")) {
        apply(plugin = "io.freefair.lombok")
        lombok {
            version.set("1.18.24")
            disableConfig.set(true)
        }
    }

    // Source Compatibility
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
        withJavadocJar()
    }

    // Dependencies
    dependencies {
        // Logging
        api(group = "org.slf4j", name = "slf4j-api", version = "1.7.36")

        // Testing
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.8.2")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.8.2")
    }

    publishing {
        repositories {
            maven {
                name = "maven"
                url = uri(project.mavenRepositoryUrl)
                credentials {
                    username = project.mavenRepositoryUsername
                    password = project.mavenRepositoryPassword
                }
            }
        }
        publications {
            create<MavenPublication>("main") {
                from(components["java"])
                artifactId = project.artifactId
                pom.default()
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["main"])
    }

    // Source encoding
    tasks {
        // javadoc / html5 support
        withType<Javadoc> {
            // hide javadoc warnings (a lot from delombok)
            (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")

            if (JavaVersion.current().isJava9Compatible) {
                (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
            }
        }

        // compile options
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        // javadoc & delombok
        javadoc {
            if (!project.name.contains("kotlin")) {
                val delombok by getting(io.freefair.gradle.plugins.lombok.tasks.Delombok::class)
                dependsOn(delombok)
                source(delombok)
            }

            options {
                title = "${project.artifactId} (v${project.version})"
                windowTitle = "${project.artifactId} (v${project.version})"
                encoding = "UTF-8"
            }
        }

        // test
        test {
            useJUnitPlatform {
                includeTags("unittest")
                excludeTags("integration")
            }
        }
    }
}

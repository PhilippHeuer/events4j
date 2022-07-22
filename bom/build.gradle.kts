plugins {
    `java-platform`
}

projectConfiguration {
    artifactId.set("events4j-bom")
    artifactDisplayName.set("Events4J - BOM")
    artifactDescription.set("Events4J - Bill of materials")
    type.set(me.philippheuer.projectcfg.domain.ProjectType.LIBRARY)
}

dependencies {
    constraints {
        api(project(":api"))
        api(project(":core"))
        api(project(":handler-simple"))
        api(project(":handler-reactor"))
        api(project(":handler-spring"))
        api(project(":kotlin"))
    }
}

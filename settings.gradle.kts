pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
}

rootProject.name = "events4j"

include(
	"bom",
	"api",
	"core",
	"handler-simple",
	"handler-reactor",
	"handler-spring",
	"kotlin"
)

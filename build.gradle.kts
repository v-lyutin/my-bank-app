plugins {
	java
	id("org.springframework.boot") version "3.5.7" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
}

subprojects {

	group = "com.amit.mybankapp"
	version = "0.0.1"

	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")

	repositories {
		mavenCentral()
	}

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

}

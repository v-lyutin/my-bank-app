plugins {
    `java-library`
}

dependencies {
    api("jakarta.validation:jakarta.validation-api")

    implementation("org.springframework:spring-webmvc")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
}
plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":my-bank-api-errors"))
    implementation(project(":my-bank-commons"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.liquibase:liquibase-core")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql")
}
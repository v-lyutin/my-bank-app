plugins {
    `java-library`
}

dependencies {
    api(project(":my-bank-commons"))
    implementation(project(":my-bank-api-errors"))

    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
}
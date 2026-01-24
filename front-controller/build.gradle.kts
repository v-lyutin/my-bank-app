plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":my-bank-commons"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
plugins {
    id("com.github.ben-manes.versions") version "0.51.0"
    application
    id("java")
    id("org.sonarqube") version "7.2.2.6593"
    checkstyle
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

sonar {
    properties {
        property("sonar.projectKey", "CharNatalia_java-project-72")
        property("sonar.organization", "charnatalia")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = file("app/config/checkstyle/checkstyle.xml")
}
plugins {
    id("com.github.ben-manes.versions") version "0.51.0"
    application
    id("java")
    id("org.sonarqube") version "7.2.2.6593"
    checkstyle
    jacoco
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
    implementation("info.picocli:picocli:4.7.7")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.0")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.16.2")
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = file("$rootDir/config/checkstyle/checkstyle.xml")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

sonarqube {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "./build/reports/jacoco/test/jacocoTestReport.xml")
    }
}
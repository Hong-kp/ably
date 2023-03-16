import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.7.10" // entity noarg 포함
    kotlin("plugin.allopen") version "1.7.10"
    kotlin("kapt") version "1.7.10"
    kotlin("plugin.noarg") version "1.7.10" // noarg 추가를 위함 by reflection
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("com.ably.project.infrastructure.aop.annotation.NoArg")
//	invokeInitializers = false
}

group = "com.ably"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework:spring-aspects")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("com.h2database:h2")

//	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.4")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.querydsl:querydsl-jpa")
    implementation("junit:junit:4.13.2")
    implementation("org.modelmapper:modelmapper:3.1.0")
    implementation("org.apache.commons:commons-dbcp2")
    implementation("mysql:mysql-connector-java")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.368")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("no.api.freemarker:freemarker-java8:2.1.0")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1")
    implementation("io.sentry:sentry-spring-boot-starter:5.7.2")
    implementation("io.sentry:sentry-logback:5.7.2")

    // feign controller
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.4")
    implementation("io.github.openfeign:feign-gson:11.9.1")
    implementation("io.github.openfeign:feign-jackson:11.9.1")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // for m1
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")

    // JPA parameter logging
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")

    kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa") // 2)

    // 3)
    sourceSets.main {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("$buildDir/generated/source/kapt/main")
        }
    }

    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("com.auth0:java-jwt:4.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")
    testImplementation("org.assertj:assertj-core:3.23.1")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:1.7.10")
    }
}

sourceSets.main {
    resources.setSrcDirs(listOf("src/main/resources"))
}
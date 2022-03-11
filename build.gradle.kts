plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    `java-library`
}

group = "org.vnovolotskiy"
version = getVersionFromChangelog()

repositories {
    //        mavenCentral() //needed only locally w/o VPN available
    maven { setUrl("https://nexus-new.tcsbank.ru/repository/mvn-maven-proxy") }
    maven { setUrl("https://nexus-new.tcsbank.ru/repository/mvn_processing_snapshot") }
    maven { setUrl("https://nexus-new.tcsbank.ru/repository/mvn_processing_release") }
}

dependencies {
    implementation(kotlin("stdlib"))
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    implementation("ru.tinkoff.acm.acm-billing-app:acm-billing-api-model:0.5.0-RELEASE")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

fun getVersionFromChangelog(): String {

    val versionPrefix = "# Version "
    var commentLine = false
    val line = File(rootDir, "CHANGELOG.md").readLines().find {
        if (it.matches("<!--\\.*".toRegex())) {
            commentLine = true
        }
        if (!commentLine && it.startsWith(versionPrefix)) {
            return@find true
        }
        if (it.matches("\\.*-->".toRegex())) {
            commentLine = false
        }
        false
    }
    return line?.replaceFirst(versionPrefix, "")
        ?.trim()
        ?.let { it.substring(0, it.indexOf(" ")) }
        .also { println("Using version $it") } ?: throw StopActionException("Version could not be found")
}
import com.expediagroup.graphql.plugin.gradle.config.GraphQLScalar
val ktorVersion = "2.1.2"
val jacksonVersion = "2.10.5"
val konfigVersion = "1.6.10.0"
val kotlinLoggerVersion = "1.8.3"
val resilience4jVersion = "1.5.0"
val logstashVersion = "7.2"
val logbackVersion = "1.2.9"
val flywayVersion = "6.5.0"
val hikariVersion = "3.4.5"
val kotliqueryVersion = "1.3.1"
val httpClientVersion = "4.5.13"
val schemaValidationVersion = "1.0.69"
val prometheusVersion = "1.7.0"
val graphQLVersion = "6.4.0"
val mainClass = "no.nav.medlemskap.barnebriller.ApplicationKt"

plugins {
    kotlin("jvm") version "1.9.20"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.expediagroup.graphql") version "6.4.0"
}

group = "no.nav.medlemskap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://packages.confluent.io/maven/")
    maven("https://repo.adeo.no/repository/maven-releases")
    maven("https://repo.adeo.no/repository/nexus2-m2internal")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("org.apache.httpcomponents:httpclient:$httpClientVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")

    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")

    implementation("io.micrometer:micrometer-registry-prometheus:$prometheusVersion")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.natpryce:konfig:$konfigVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggerVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    // 2.8.0 er tilgjengelig, burde kanskje oppdatere

    //GRAFQL

    // GraphQL
    implementation("com.expediagroup:graphql-kotlin-ktor-client:$graphQLVersion")
    implementation("com.expediagroup:graphql-kotlin-client-jackson:$graphQLVersion")


    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")

}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "20"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to mainClass
                )
            )
        }
    }

    java{
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20

    }
    test {
        useJUnitPlatform()
        //Trengs inntil videre for bytebuddy med java 16, som brukes av mockk.
        java.targetCompatibility = JavaVersion.VERSION_20
        java.sourceCompatibility = JavaVersion.VERSION_20
        jvmArgs = listOf("-Dnet.bytebuddy.experimental=true")
    }
    graphql {
        client {
            schemaFile = file("${project.projectDir}/src/main/resources/pdl/schema.graphql")
            queryFiles = listOf(
                file("${project.projectDir}/src/main/resources/pdl/hentPerson.graphql"),
                file("${project.projectDir}/src/main/resources/pdl/medlemskapHentBarn.graphql"),
                file("${project.projectDir}/src/main/resources/pdl/medlemskapHentVergeEllerForelder.graphql"),
            )
            customScalars = listOf(
                GraphQLScalar(
                    "Long",
                    "kotlin.Long",
                    "no.nav.medlemskap.barnebriller.service.pdl.LongConverter"
                ),
                GraphQLScalar(
                    "Date",
                    "java.time.LocalDate",
                    "no.nav.medlemskap.barnebriller.service.pdl.DateConverter"
                ),
                GraphQLScalar(
                    "DateTime",
                    "java.time.LocalDateTime",
                    "no.nav.medlemskap.barnebriller.service.pdl.DateTimeConverter"
                ),
            )
            packageName = "no.nav.medlemskap.barnebriller.pdl.generated"
        }
    }

}

application {
    mainClass.set("no.nav.medlemskap.barnebriller.ApplicationKt")
}



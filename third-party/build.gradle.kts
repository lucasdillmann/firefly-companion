import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.open.api.generator)
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    api(project(":business"))

    api(libs.okhttp3)
    api(libs.moshi.kotlin)
    api(libs.moshi.adapters)
    api(libs.mapstruct)

    kapt(libs.mapstruct.processor)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }

    sourceSets {
        main {
            kotlin.srcDir("${projectDir.path}/build/generated/src/main/kotlin")
        }
    }
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }

    withType<KaptGenerateStubsTask> {
        dependsOn(openApiGenerate)
    }
}

openApiGenerate {
    inputSpecRootDirectory = "${projectDir.path}/src/main/resources/spec"
    outputDir = "${projectDir.path}/build/generated"
    packageName = "br.com.dillmann.fireflycompanion.thirdparty.firefly"
    generatorName = "kotlin"
    configOptions.putAll(
        mapOf(
            "dateLibrary" to "java8",
            "modelMutable" to "true",
            "enumPropertyNaming" to "UPPERCASE",
        )
    )
    typeMappings.putAll(mapOf(
        "number" to BigDecimal::class.qualifiedName,
        "double" to BigDecimal::class.qualifiedName,
        "float" to BigDecimal::class.qualifiedName,
        "amount" to BigDecimal::class.qualifiedName,
        "date-time" to OffsetDateTime::class.qualifiedName,
        "date" to LocalDate::class.qualifiedName,
        "time" to LocalTime::class.qualifiedName,
    ))
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.open.api.generator)
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    api(project(":business"))

    api(libs.okhttp3)
    api(libs.moshi.kotlin)
    api(libs.moshi.adapters)
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
}

openApiGenerate {
    inputSpecRootDirectory = "${projectDir.path}/src/main/resources/spec"
    outputDir = "${projectDir.path}/build/generated"
    packageName = "br.com.dillmann.fireflycompanion.thirdparty.firefly"
    generatorName = "kotlin"
    configOptions.putAll(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
        )
    )
    typeMappings.putAll(mapOf(
        "number" to "java.math.BigDecimal",
        "double" to "java.math.BigDecimal",
        "float" to "java.math.BigDecimal",
    ))
}

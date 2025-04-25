plugins {
    kotlin("jvm")
}

group = "kafka.course"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val generatedDir = file("${buildDir}/generated/demo/opensearch")

sourceSets {
    main {
        kotlin.srcDir(generatedDir) // Add the generated directory to the main source set
    }
}

dependencies {
    implementation(project(":config"))

    // https://mvnrepository.com/artifact/org.opensearch.client/opensearch-rest-high-level-client
    implementation("org.opensearch.client:opensearch-rest-high-level-client:1.3.2")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}

tasks.register("generateEnvConfig") {
    doLast {
        val envFile = file(".env")
        val envMap = mutableMapOf<String, String>()

        if (!envFile.exists()) throw Error("can not find $envFile")

        envFile.forEachLine { line ->
            // Ignore comments and empty lines
            if (line.isNotBlank() && !line.startsWith("#")) {
                val (key, value) = line.split("=", limit = 2).map { it.trim() }
                println("key = $key")
                println("value = $value")
                envMap[key] = value
            }
        }

        val buildConfigFile = file("$generatedDir/EnvConfig.kt")
        buildConfigFile.parentFile.mkdirs()
        buildConfigFile.writeText(
            """
            package demo.opensearch 

            object EnvConfig {
                ${envMap.entries.joinToString("\n") { "const val ${it.key} = \"${it.value}\"" }}
            }
        """.trimIndent()
        )
    }
}

tasks.compileKotlin {
    dependsOn("generateEnvConfig")
}
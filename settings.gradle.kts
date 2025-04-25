plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kafka-beginners-course"
include("kafka-basics")
include("kafka-producer-wikimedia")
include("config")
include("kafka-consumer-opensearch")
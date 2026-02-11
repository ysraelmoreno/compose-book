plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

group = "com.ysraelmorenopkg.composebook"
version = "0.1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(libs.junit)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = "com.ysraelmorenopkg.composebook"
            artifactId = "composebook-core"
            version = "0.1.0-SNAPSHOT"  // Must match the module version
        }
    }
}
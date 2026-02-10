plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

group = "com.ysraelmorenopkg.storybook"
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
            
            groupId = "com.ysraelmorenopkg.storybook"
            artifactId = "storybook-core"
            version = "0.1.0-SNAPSHOT"
        }
    }
}
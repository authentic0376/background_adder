plugins {
    application
}

group = "com.sprain6628"
version = "0.2.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("BackgroundAdder")
    applicationName = project.name // project.name defaults to the root directory name if not explicitly set
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
                "Implementation-Title" to project.name,       // Project name
                "Implementation-Version" to project.version,  // Project version
                "Main-Class" to application.mainClass.get(),  // Reference to application.mainClass
                "Implementation-Vendor" to "sprain6628",      // Project owner/developer name
                "Built-By" to System.getProperty("user.name") // Name of the user who built the project
        )
    }
}

// Task to generate MSI installer
tasks.register<Exec>("jpackage") {
    dependsOn(tasks.jar) // Ensures `jar` task is executed first

    val jarFile = tasks.jar.get().archiveFile.get().asFile // Path to the JAR file
    val outputDir = layout.buildDirectory.dir("jpackage").get().asFile // Directory for packaging output

    doFirst {
        outputDir.mkdirs() // Create output directory
    }

    commandLine(
            "jpackage",
            "--input", jarFile.parent,              // Directory containing the JAR file
            "--main-jar", jarFile.name,            // Main JAR file
            "--name", project.name,                // Application name
            "--app-version", project.version,
            "--type", "msi",                       // Generate MSI installer
            "--dest", outputDir.absolutePath,      // Output directory
            "--java-options", "-Xmx512m",          // JVM options
            "--icon", "src/main/resources/background adder.ico", // Icon file (optional)
            "--win-menu",                          // Add Windows menu entry
            "--win-shortcut",                      // Create Windows shortcut
            "--win-dir-chooser"                    // Enable installation directory chooser
    )
}

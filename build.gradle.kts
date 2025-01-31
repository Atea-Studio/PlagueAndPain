group = "fr.ateastudio.disease"
version = "1.0-SNAPSHOT" // TODO: Change this to your addon version

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.paperweight)
    alias(libs.plugins.nova)
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(libs.nova)
}


addon {
    name = "Disease"
    version = project.version.toString()
    main = "fr.ateastudio.disease.Disease"
    authors = listOf("Katalijst")
    description = "Disease Server Addon"
    website = "https://atea-studio.fr/nova-addons"
    prefix = "Disease"
    
    // output directory for the generated addon jar is read from the "outDir" project property (-PoutDir="...")
    val outDir = project.findProperty("outDir")
    if (outDir is String)
        destination.set(File(outDir))
}

afterEvaluate {
    tasks.getByName<Jar>("jar") {
        archiveClassifier = ""
    }
}
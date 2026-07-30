group = "fr.ateastudio.plagueandpain"
version = "1.1.0"

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.nova)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    implementation(libs.nova)
}


addon {
    name = "PlagueAndPain"
    version = project.version.toString()
    main = "fr.ateastudio.plagueandpain.PlagueAndPain"
    authors = listOf("Katalijst")
    description = "Disease and injuries Server Addon"
    website = "https://atea-studio.fr/nova-addons"
    prefix = "Plague&Pain"
    bootstrapper = "fr.ateastudio.plagueandpain.PlagueAndPainBootstrap"
    
    
    // output directory for the generated addon jar is read from the "outDir" project property (-PoutDir="...")
    val outDir = project.findProperty("outDir")
    if (outDir is String)
        destination.set(File(outDir))
}
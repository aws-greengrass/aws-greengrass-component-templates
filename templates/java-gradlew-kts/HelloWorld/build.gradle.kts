import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.internal.Try

buildscript {
    dependencies {
        // This is so we can parse the gdk-config.json file and get the version, author, bucket, and region information
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")
    }
}

plugins {
    kotlin("jvm") version "1.6.10"
    id("application")
    id("java")
    id("idea")

    // This creates the fat JAR
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.hello"
description = "HelloWorld"

application.mainClass.set("com.hello.App")

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

// NOTE: Version number is pulled from gdk-config.json. Change this value to override.
val componentVersionOverride: String? = null

// Set this value to true if you want to be able to use the NEXT_PATCH value in gdk-config.json.
// If this is false and gdk-config.json contains a NEXT_PATCH value, the build will fail.
// Generally during development this should be set to true. When committing a release, this should be set to false.
val allowVersionPlaceholder = true

// This is the version number used in the JAR name when building with the NEXT_PATCH version value.
// If NEXT_PATCH is set and allowVersionPlaceholder is true the JAR will be called ${project.description}-NEXT_PATCH.jar
// This is done so gdk can find the correct JAR after a build.
val nextPatchTempVersionNumber = "1.0.0"

tasks.shadowJar {
    // Get either the specified component version, NEXT_PATCH, or the default version
    val componentVersion = componentVersionOverride ?: getComponentVersion()
    archiveFileName.set("${project.description}-$componentVersion.jar")
}

tasks.register<Exec>("publish") {
    description = "Build the component, if necessary, and publish it as a Greengrass component"
    // gdk component publish doesn't always do a build but this task does
    dependsOn("build")

    doFirst {
        // Check to make sure that all the values are filled in. If they aren't will let the user know what to do.
        checkForPlaceholders()
    }

    workingDir = file(".")

    // Finally, we'll call gdk component publish to do the real work of publishing the component for us
    commandLine = if (Os.isFamily(Os.FAMILY_WINDOWS)) listOf("cmd", "/c") else listOf("/bin/bash", "-c")
    commandLine = commandLine + listOf("$gdk component publish")
}

val configFile = "gdk-config.json"
val versionPlaceholder = "NEXT_PATCH"
val authorPlaceholder = "<PLACEHOLDER_AUTHOR>"
val bucketPlaceholder = "<PLACEHOLDER_BUCKET>"
val regionPlaceholder = "<PLACEHOLDER_REGION>"
val gdkCliConfigurationFileDocsUrl =
    "https://docs.aws.amazon.com/greengrass/v2/developerguide/gdk-cli-configuration-file.html"

fun getTopLevelComponent(): JsonObject {
    // Reference the gdk-config.json file
    return Try.ofFailable { file(configFile) }
        // Get the entire file as text
        .tryMap { it.readText() }
        // Parse the text as JSON as a JSON object
        .tryMap { parseToJsonElement(it) as JsonObject }
        // Get the top level component element as a JSON object
        .tryMap { it["component"] as JsonObject }
        // Fail here if anything above went wrong
        .mapFailure { throw IllegalStateException("Failed to parse $configFile [${it.message}]") }
        // If we get here everything was successful, and we can unwrap the Try
        .get()
}

fun getComponent(): JsonObject {
    val topLevelComponent = getTopLevelComponent()

    // Sanity check to make sure we are working with a file that is set up as we expected
    if (topLevelComponent.keys.size > 1) throw IllegalStateException("Found more than one component in $configFile")
    if (topLevelComponent.keys.isEmpty()) throw IllegalStateException("Did not find any components in $configFile")

    // The only key should be the component name
    val componentName = topLevelComponent.keys.first()

    // Get the user specified component element as a JsonObject
    return Try.ofFailable { topLevelComponent[componentName] as JsonObject }
        // Fail here if anything above went wrong
        .mapFailure { throw IllegalStateException("Failed process the component information [${it.message}] in $configFile") }
        // If we get here everything was successful, and we can unwrap the Try
        .get()
}

fun getComponentVersion(): String {
    val component = getComponent()

    // Get the version element as a JSON primitive
    val componentVersion = Try.ofFailable { component["version"] as JsonPrimitive }
        // Extract the string from the primitive object
        .tryMap { it.content }
        // Fail here if anything above went wrong
        .mapFailure { throw IllegalStateException("Component version could not be found in $configFile. This needs to be set to build the component.") }
        // If we get here everything was successful, and we can unwrap the Try
        .get()

    // If the version is NEXT_PATCH, and we're allowing the placeholder then set the version to the temp version number (usually "1.0.0")
    //   otherwise just use the value they specified
    if (allowVersionPlaceholder) return if (componentVersion == versionPlaceholder) nextPatchTempVersionNumber else componentVersion

    // At this point the NEXT_PATCH version is NOT allowed

    // If the version isn't NEXT_PATCH just return it
    if (versionPlaceholder != componentVersion) return componentVersion

    // Throw an exception if the version is NEXT_PATCH since it's not allowed here
    throw IllegalStateException("Component version is still set to $versionPlaceholder in $configFile. This needs to be updated to build the component or allowVersionPlaceholder needs to be set to true in build.gradle.kts.")
}

val documentationMessage = "See $gdkCliConfigurationFileDocsUrl for information on how to configure this."

fun checkForPlaceholders() {
    val publish = getPublishJsonObjectOrThrow()

    val authorIsPlaceholder = isElementPlaceholder(getComponent(), "author", authorPlaceholder)
    val bucketIsPlaceholder = isElementPlaceholder(publish, "bucket", bucketPlaceholder)
    val regionIsPlaceholder = isElementPlaceholder(publish, "region", regionPlaceholder)

    var errors = emptyList<String>()

    if (authorIsPlaceholder) errors = errors + "Author information is still set to $authorPlaceholder in $configFile."
    if (bucketIsPlaceholder) errors = errors + "Bucket information is still set to $bucketPlaceholder in $configFile."
    if (regionIsPlaceholder) errors = errors + "Region information is still set to $regionPlaceholder in $configFile."

    if (errors.isNotEmpty()) {
        // If there were any errors gather them with the documentation link and throw an exception
        errors = errors + documentationMessage
        throw IllegalStateException(errors.joinToString("\n"))
    }
}

fun isElementPlaceholder(
    jsonObject: JsonObject,
    elementName: String,
    placeholder: String
): Boolean {
    return Try.ofFailable { jsonObject }
        // Get the element by name as a JSON primitive
        .tryMap { it[elementName] as JsonPrimitive }
        // Extract the string from the primitive object
        .tryMap { it.content }
        // Determine if it is the placeholder value
        .tryMap { placeholder == it }
        // Fail here if anything above went wrong
        .mapFailure { throw IllegalStateException("The value for $elementName in $configFile is not set. $documentationMessage") }
        // If we get here everything was successful, and we can unwrap the Try
        .get()
}

fun getPublishJsonObjectOrThrow(): JsonObject {
    return Try.ofFailable { getComponent() }
        // Get the publish elements as a JSON object
        .tryMap { it["publish"] as JsonObject }
        // Fail here if anything above went wrong
        .mapFailure { throw IllegalStateException("Failed to parse publish information in $configFile [${it.message}]. $documentationMessage") }
        // If we get here everything was successful, and we can unwrap the Try
        .get()
}

// This can be changed to use gdk in different locations when needed for gdk debugging
val gdk = "gdk"

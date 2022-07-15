import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.openjfx.javafxplugin") version "0.0.13"
    application
    id("com.gluonhq.gluonfx-gradle-plugin") version "1.0.14"
}

group = "com.dxfeed"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://nexus.gluonhq.com/nexus/content/repositories/releases")
}

javafx {
    version = "12"
    modules("javafx.controls")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.gluonhq:charm-glisten:6.1.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val graalVMHome = System.getenv("GRAALVM_HOME") ?: (gradle.gradleUserHomeDir.parent + "/GraalVM")

application {
    mainClass.set("com.dxfeed.DxFeedGraalGluonKotlinApp")
    @Suppress("DEPRECATION")
    mainClassName = mainClass.get()
}

val toReflect = listOf(
        //@Service, @ServiceProvider, @SupersedesService
        //dxlib
        "com.devexperts.services.Service",
        "com.devexperts.services.ServiceProvider",
        "com.devexperts.services.SupersedesService",
        "com.devexperts.services.Services",

        //dxfeed-iml
        "com.dxfeed.api.impl.DXFeedScheme",

        //@Configurable
        "com.devexperts.connector.proto.Configurable",
        "com.devexperts.connector.proto.ConfigurableObject",

        //proto/codecs
        "com.devexperts.connector.proto.ApplicationConnectionFactory",
        "com.devexperts.connector.codec.CodecConnectionFactory",
//        "com.devexperts.connector.codec.delayed.DelayedConnectionFactory",
        "com.devexperts.connector.codec.shaped.ShapedConnectionFactory",

        //qd-core
        "com.devexperts.qd.qtp.AgentAdapter",
//        "com.devexperts.qd.qtp.AgentAdapter.Factory",
//        "com.devexperts.qd.qtp.DistributorAdapter.Factory",
//        "com.devexperts.qd.qtp.MessageAdapter.AbstractFactory",
        "com.devexperts.qd.qtp.MessageAdapterConnectionFactory",

        //proto-ssl
        "com.devexperts.connector.codec.ssl.SSLConnectionFactory",

        //qd-rmi
//        "com.devexperts.rmi.impl.RMIConnectorInitializer.AdapterFactory",

        //misc
        "java.net.Inet6Address",
        )



gluonfx {
    // target = "ios" // Uncomment to enable iOS
    target = "android" // Uncomment to enable Android
    attachConfig.version = "4.0.9"
    attachConfig.services("display", "lifecycle", "statusbar", "storage")
    graalvmHome = graalVMHome
    reflectionList = toReflect
    jniList = toReflect
    //compilerArgs = listOf("--initialize-at-build-time=sun.nio.ch.Net.initIDs")
    //compilerArgs = listOf("--initialize-at-build-time=java.net.Inet6Address")
    //compilerArgs = listOf("--trace-class-initialization=java.net.Inet6Address")
}

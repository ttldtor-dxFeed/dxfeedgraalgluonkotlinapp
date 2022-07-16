import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.openjfx.javafxplugin") version "0.0.13"
    application
    //id("com.gluonhq.gluonfx-gradle-plugin") version "1.0.14"
    id("com.gluonhq.gluonfx-gradle-plugin") version "1.0.9"
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.4")
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val graalVMHome = System.getenv("GRAALVM_HOME") ?: (gradle.gradleUserHomeDir.parent + "/GraalVM")

println("!!!!GRAALVM_HOME: $graalVMHome !!!")

application {
    mainClass.set("com.dxfeed.DxFeedGraalGluonKotlinApp")
    @Suppress("DEPRECATION")
    mainClassName = mainClass.get()
}

val toReflect = listOf(
        //@Service, @ServiceProvider, @SupersedesService, @SpecificSubscriptionFilter
        //dxlib
        "com.devexperts.services.Service",
        "com.devexperts.services.ServiceProvider",
        "com.devexperts.services.SupersedesService",
        "com.devexperts.services.Services",
        "com.devexperts.services.StartupService",
        "com.devexperts.services.OverrideURLClassLoader",

        //dxfeed-api
        "com.dxfeed.api.DXEndpoint.Builder",

        //dxfeed-impl
        "com.dxfeed.api.impl.DXEndpointImpl.BuilderImpl",
        "com.dxfeed.api.impl.DXEndpointImpl.BuilderRMIImpl",
        "com.dxfeed.api.impl.DXFeedScheme",
        "com.dxfeed.api.impl.FilterFactoryImpl",
        "com.dxfeed.api.impl.HistorySubscriptionFilterImpl",
        "com.dxfeed.event.candle.CandleFactoryImpl",
        "com.dxfeed.event.market.MarketFactoryImpl",
        "com.dxfeed.event.misc.MiscFactoryImpl",
        "com.dxfeed.event.option.OptionFactoryImpl",

        //dxfeed-ipf-filter
        "com.dxfeed.ipf.filter.IPFFilterFactory",

        //dxfeed-scheme
        "com.dxfeed.scheme.impl.DXSchemeFactory",

        //dxfeed-webservice-impl
        "com.dxfeed.webservice.comet.DataService",

        //qd-core
        "com.devexperts.qd.DataScheme",
        "com.devexperts.qd.DataSchemeFactory",
        "com.devexperts.qd.SubscriptionFilterFactory",
        "com.devexperts.qd.qtp.FieldReplacer.Factory",
        "com.devexperts.qd.qtp.QDEndpoint.Builder",
        "com.devexperts.qd.qtp.SubscriptionFilterFactory",
        "com.devexperts.qd.qtp.auth.ChannelShapersFactory",
        "com.devexperts.qd.qtp.auth.ConsoleLoginHandlerFactory",
        "com.devexperts.qd.qtp.auth.QDAuthRealmFactory",
        "com.devexperts.qd.qtp.auth.QDLoginHandlerFactory",
        "com.devexperts.qd.qtp.fieldreplacer.SetFieldReplacer.Factory",
        "com.devexperts.qd.qtp.fieldreplacer.TimeFieldReplacer.Factory",
        "com.devexperts.qd.spi.QDFilterFactory",

        //qd-rmi
        "com.devexperts.rmi.RMIEndpoint.Builder",
        "com.devexperts.rmi.impl.DefaultLoadBalancerFactory",
        "com.devexperts.rmi.task.RMILoadBalancerFactory",
        "com.devexperts.rmi.security.SecurityController",

        //@Configurable
        "com.devexperts.connector.proto.Configurable",
        "com.devexperts.connector.proto.ConfigurableObject",

        //proto
        "com.devexperts.connector.proto.ApplicationConnectionFactory",
        "com.devexperts.connector.codec.CodecConnectionFactory",
//        "com.devexperts.connector.codec.delayed.DelayedConnectionFactory",
        "com.devexperts.connector.codec.shaped.ShapedConnectionFactory",

        //proto-ssl
        "com.devexperts.connector.codec.ssl.SSLConnectionFactory",

        //qd-core
        "com.devexperts.qd.qtp.AgentAdapter",
        "com.devexperts.qd.qtp.AgentAdapter.Factory",
        "com.devexperts.qd.qtp.DistributorAdapter",
        "com.devexperts.qd.qtp.DistributorAdapter.Factory",
        "com.devexperts.qd.qtp.MessageAdapter",
        "com.devexperts.qd.qtp.MessageAdapter.AbstractFactory",
        "com.devexperts.qd.qtp.MessageAdapterConnectionFactory",

        //qd-rmi
        "com.devexperts.rmi.impl.RMIConnectorInitializer",
        "com.devexperts.rmi.impl.RMIConnectorInitializer.AdapterFactory",

        //misc

        //kotlin.random.Random
        "kotlin.internal.jdk8.JDK8PlatformImplementations"
        )

gluonfx {
    // target = "ios" // Uncomment to enable iOS
    target = "android" // Uncomment to enable Android
    attachConfig.version = "4.0.9"
    attachConfig.services("display", "lifecycle", "statusbar", "storage")
    graalvmHome = graalVMHome
    reflectionList = toReflect
    jniList = toReflect
    compilerArgs = listOf("--allow-incomplete-classpath")
}

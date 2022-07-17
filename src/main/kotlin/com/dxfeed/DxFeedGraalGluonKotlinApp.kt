package com.dxfeed

import com.dxfeed.views.MainView
import com.gluonhq.attach.storage.StorageService
import com.gluonhq.attach.util.Services
import com.gluonhq.charm.glisten.application.AppManager
import com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.io.File

class DxFeedGraalGluonKotlinApp : Application() {
    private val appManager: AppManager = AppManager.initialize(this::postInit)

    private fun postInit(scene: Scene) {
        Swatch.BLUE.assignTo(scene)
        (scene.window as Stage).icons.add(Image(DxFeedGraalGluonKotlinApp::class.java.getResourceAsStream("/dx.png")))
    }

    override fun init() {
        super.init()

        appManager.addViewFactory(HOME_VIEW, ::MainView)
    }

    override fun start(primaryStage: Stage?) {
        appManager.start(primaryStage)
    }

    companion object {
        // HARDCODE:
        // We give access to the logs, taking into account this behavior in com.devexperts.logging.DefaultLogging:
        // `handler = new FileHandler(log_file, getLimit(Logging.LOG_MAX_FILE_SIZE_PROPERTY, errors), 2, true);`
        //
        // Please set `log.className` System property to `com.devexperts.logging.Log4j2Logging` or
        // `com.devexperts.logging.Log4jLogging` and add a valid logging properties
        private fun prepareLogFiles() {
            val documentsRootDir = Services.get(StorageService::class.java)
                    .flatMap { s: StorageService -> s.getPublicStorage("Documents") }
                    .orElseThrow { RuntimeException("Documents not available") }

            val logFilePath = "${documentsRootDir.absolutePath}${File.separator}dxfeedggkap.log"
            val errFilePath = "${documentsRootDir.absolutePath}${File.separator}dxfeedggkap.err"

            System.setProperty("log.file", logFilePath)
            System.setProperty("err.file", errFilePath)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            prepareLogFiles()
            System.setProperty("scheme", "com.dxfeed.api.impl.DXFeedScheme")
            launch(DxFeedGraalGluonKotlinApp::class.java)
        }
    }
}
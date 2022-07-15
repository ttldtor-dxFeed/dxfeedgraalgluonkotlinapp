package com.dxfeed

import com.dxfeed.views.MainView
import com.gluonhq.charm.glisten.application.AppManager
import com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.net.Inet6Address

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
        @JvmStatic
        fun main(args: Array<String>) {
            Inet6Address.getLocalHost()

            System.setProperty("scheme", "com.dxfeed.api.impl.DXFeedScheme")
            launch(DxFeedGraalGluonKotlinApp::class.java)
        }
    }
}
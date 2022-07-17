package com.dxfeed.views

import com.dxfeed.models.Logger
import com.dxfeed.models.QDService
import com.gluonhq.charm.glisten.mvc.View
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.util.Callback
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch

class LogViewListCell(list: ListView<String>) : ListCell<String>() {
    init {
        val text = Text()
        text.wrappingWidthProperty().bind(list.widthProperty().subtract(15))
        text.textProperty().bind(itemProperty())

        prefWidth = 0.0
        graphic = text
    }

    override fun updateItem(item: String?, empty: Boolean) {
        isWrapText = true

        super.updateItem(item, empty)

        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            text = item
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
class MainView : View() {
    private val logger = Logger(5000)
    private val qdService = QDService(logger)

    private val addressText = TextField("demo.dxfeed.com:7300")

    //private val addressText = TextField("208.93.103.170:7300")
    private val lastQuoteByPromiseButton = Button("LastQuoteByPromise")
    private val testQuoteSubscriptionButton = Button("TestQuoteSubscription")
    private val controls = HBox(5.0, addressText, lastQuoteByPromiseButton, testQuoteSubscriptionButton)
    private val logView = ListView(logger.logObservableList)
    private val mainLayout = VBox(10.0, controls, logView)

    init {
        addressText.prefColumnCount = 20
        center = mainLayout
        logView.prefWidth = prefWidth - 5
        logView.cellFactory = Callback {
            LogViewListCell(logView)
        }

        logger.log("Initialization")

        lastQuoteByPromiseButton.onAction = EventHandler {
            GlobalScope.launch(Dispatchers.JavaFx) {
                logger.log("LastQuoteByPromise: started")
                val quote = qdService.getLastQuoteByPromise(addressText.text, "AAPL", 20)
                logger.log("LastQuoteByPromise: " + quote?.toString())
                logger.log("LastQuoteByPromise: finished")
            }
        }

        testQuoteSubscriptionButton.onAction = EventHandler {
            GlobalScope.launch(Dispatchers.JavaFx) {
                qdService.testQuoteSubscription(addressText.text, listOf("AAPL"), 20)
            }
        }
    }
}
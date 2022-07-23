package com.dxfeed.views

import com.dxfeed.extensions.splitSymbols
import com.dxfeed.models.Logger
import com.dxfeed.models.QDService
import com.dxfeed.tools.Speedometer
import com.gluonhq.charm.glisten.mvc.View
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MainView : View() {
    private val logger = Logger(5000)
    private val speedometer = Speedometer(logger, 5000)
    private val qdService = QDService(logger, speedometer)


    private val addressLabel = Label("Address:")
    private val addressText = TextField("demo.dxfeed.com:7300")
    //private val addressText = TextField("192.168.0.149:8888")

    private val symbolsLabel = Label("Symbol(s):")
    private val symbolsText = TextField("AAPL, IBM, ETH/USD:GDAX")

    private val timeoutLabel = Label("Timeout (sec)\n0 -- Inf/default):")
    private val timeoutText = TextField("0")

    private val configGrid = GridPane()

    private val lastQuoteByPromiseButton = Button("LastQuoteByPromise")
    private val testQuoteSubscriptionButton = Button("TestQuoteSubscription")
    private val testTnsSubscriptionButton = Button("TestTnsSubscription")
    private val testNonTimeSeriesTnsSubscriptionButton = Button("TestNonTimeSeriesTnsSubscription")
    private val controls = HBox(5.0, lastQuoteByPromiseButton, testQuoteSubscriptionButton)
    private val controls2 = HBox(5.0, testTnsSubscriptionButton, testNonTimeSeriesTnsSubscriptionButton)

    private val logView = ListView(logger.logObservableList)
    private val mainLayout = VBox(10.0, configGrid, controls, controls2, logView)

    init {
        configGrid.addRow(0, addressLabel, addressText)
        configGrid.addRow(1, symbolsLabel, symbolsText)
        configGrid.addRow(2, timeoutLabel, timeoutText)

        for (rc in configGrid.rowConstraints) {
            rc.isFillHeight = true
        }

        timeoutLabel.style = "-fx-padding: -0.25em 0em -0.15em 0em;"

        addressText.prefColumnCount = 20
        symbolsText.prefColumnCount = 20
        timeoutText.prefColumnCount = 20

        for (b in listOf(lastQuoteByPromiseButton, testQuoteSubscriptionButton, testTnsSubscriptionButton,
                testNonTimeSeriesTnsSubscriptionButton)) {
            b.prefWidth = 200.0
            b.prefHeight = 28.0
            b.style = "-fx-padding: -0.25em 0em -0.15em 0em;"
        }

        center = mainLayout
        logView.prefWidth = prefWidth - 5

        logView.cellFactory = Callback {
            LogViewListCell(logView)
        }

        logger.log("Initialization")

        lastQuoteByPromiseButton.onAction = EventHandler {
            GlobalScope.launch(Dispatchers.JavaFx) {
                logger.log("LastQuoteByPromise: started")
                val quote = qdService.getLastQuoteByPromise(addressText.text, symbolsText.text.splitSymbols()[0],
                        timeoutText.text.toLongOrNull() ?: 20)
                logger.log("LastQuoteByPromise: " + quote?.toString())
                logger.log("LastQuoteByPromise: finished")
            }
        }

        testQuoteSubscriptionButton.onAction = EventHandler {
            GlobalScope.launch(Dispatchers.JavaFx) {
                qdService.testQuoteSubscription(addressText.text, symbolsText.text.splitSymbols(),
                        timeoutText.text.toLongOrNull() ?: 20)
            }
        }

        testTnsSubscriptionButton.onAction = EventHandler {
            GlobalScope.launch(Dispatchers.JavaFx) {
                qdService.testTnsSubscription(addressText.text, symbolsText.text.splitSymbols(),
                        timeoutText.text.toLongOrNull() ?: 20)
            }
        }

        testNonTimeSeriesTnsSubscriptionButton.onAction = EventHandler {
            GlobalScope.launch(Dispatchers.JavaFx) {
                qdService.testNonTimeSeriesTnsSubscription(addressText.text, symbolsText.text.splitSymbols(),
                        timeoutText.text.toLongOrNull() ?: 20)
            }
        }
    }
}
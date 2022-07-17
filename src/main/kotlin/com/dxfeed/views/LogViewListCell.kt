package com.dxfeed.views

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.text.Text

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
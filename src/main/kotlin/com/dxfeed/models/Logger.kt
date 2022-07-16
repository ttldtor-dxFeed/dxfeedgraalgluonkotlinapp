package com.dxfeed.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Logger(private val logSize: Int) {
    var logObservableList: ObservableList<String> = FXCollections.observableArrayList()
        private set

    @OptIn(DelicateCoroutinesApi::class)
    fun log(string: String) {
        GlobalScope.launch(Dispatchers.JavaFx) {
            if (logObservableList.size == logSize) {
                logObservableList.removeAt(logSize - 1)
            }

            logObservableList.add(0, "${ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)} > $string")
        }
    }
}
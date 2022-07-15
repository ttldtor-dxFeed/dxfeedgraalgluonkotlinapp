package com.dxfeed.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Logger(private val logSize: Int) {
    var logObservableList: ObservableList<String> = FXCollections.observableArrayList()
        private set

    private fun now(): String {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_TIME)
    }

    fun log(string: String) {
        if (logObservableList.size == logSize) {
            logObservableList.removeAt(logSize - 1)
        }

        logObservableList.add(0, "${now()} > $string")
    }
}
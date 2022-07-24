package com.dxfeed.tools

import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs

@OptIn(DelicateCoroutinesApi::class)
class IncrementedParametersGapDetector(private val logger: Logger, private var enabled: Boolean) {
    private val mutex = Mutex()
    private val lastValues = mutableMapOf<String, Number>()
    private var firstCheck = true

    fun check(id: String, paramGetter: () -> Number) {
        GlobalScope.launch(Dispatchers.IO) {
            mutex.withLock {
                if (!enabled) {
                    return@launch
                }

                val currentValue = paramGetter()
                val lastValue = lastValues[id]

                if (lastValue != null) {
                    if (abs(1.0 - abs(currentValue.toDouble() - lastValue.toDouble())) > 0.000001) {
                        logger.log("GapDetector: $id: ${"%.3f".format(lastValue)} - ${"%.3f".format(currentValue)}")
                    }
                } else {
                    firstCheck = false
                }

                lastValues[id] = currentValue
            }
        }
    }

    fun enable(e: Boolean) {
        GlobalScope.launch(Dispatchers.JavaFx) {
            mutex.withLock {
                enabled = e;
            }
        }
    }
}
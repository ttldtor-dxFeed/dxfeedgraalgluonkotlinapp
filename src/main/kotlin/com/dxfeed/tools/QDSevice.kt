package com.dxfeed.models

import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Quote
import com.dxfeed.event.market.TimeAndSale
import com.dxfeed.tools.IncrementedParametersGapDetector
import com.dxfeed.tools.Logger
import com.dxfeed.tools.Speedometer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class QDService(private val logger: Logger, private val speedometer: Speedometer, private val gapDetector: IncrementedParametersGapDetector) {
    suspend fun getLastQuoteByPromise(address: String, symbol: String, timeout: Long): Quote? = withContext(Dispatchers.IO) {
        val calculatedTimout = if (timeout == 0L) 1000000 else timeout

        DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createSubscription(Quote::class.java).use {
                        val promise = endpoint.feed
                                .getLastEventPromise(Quote::class.java, symbol)

                        if (!promise.awaitWithoutException(calculatedTimout, TimeUnit.SECONDS)) {
                            logger.log("QDService: getLastQuoteByPromise: timeout")

                            return@withContext null
                        }

                        return@withContext promise.result
                    }
                }
    }

    suspend fun testQuoteSubscription(address: String, symbols: List<String>, timeout: Long) = withContext(Dispatchers.IO) {
        val calculatedTimout = if (timeout == 0L) 1000000 else timeout

        logger.log("QDService: QuoteSub: Connecting")
        DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createSubscription(Quote::class.java).use { sub ->
                        sub.addEventListener { items ->
                            speedometer.addEvents(items.size.toLong())
                        }
                        sub.addSymbols(symbols)

                        delay(calculatedTimout * 1000)

                        logger.log("QDService: QuoteSub: Disconnecting")
                    }
                }
    }

    suspend fun testHistoryTnsSubscription(address: String, symbols: List<String>, timeout: Long) = withContext(Dispatchers.IO) {
        val calculatedTimout = if (timeout == 0L) 1000000 else timeout

        logger.log("QDService: TnsHistorySub: Connecting")
        DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createTimeSeriesSubscription(TimeAndSale::class.java).use { sub ->
                        sub.fromTime = 0L
                        sub.addEventListener { items ->
                            speedometer.addEvents(items.size.toLong())
                            items.forEach {
                                gapDetector.check("TnsHistorySub", it::getPrice)
                            }
                        }
                        sub.addSymbols(symbols)

                        delay(calculatedTimout * 1000)

                        logger.log("QDService: TnsHistorySub: Disconnecting")
                    }
                }
    }

    suspend fun testStreamTnsSubscription(address: String, symbols: List<String>, timeout: Long) = withContext(Dispatchers.IO) {
        val calculatedTimout = if (timeout == 0L) 1000000 else timeout

        logger.log("QDService: TnsStreamSub: Connecting")
        DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createSubscription(TimeAndSale::class.java).use { sub ->
                        sub.addEventListener { items ->
                            speedometer.addEvents(items.size.toLong())
                            items.forEach {
                                gapDetector.check("TnsStreamSub", it::getPrice)
                            }
                        }
                        sub.addSymbols(symbols)

                        delay(calculatedTimout * 1000)

                        logger.log("QDService: TnsStreamSub: Disconnecting")
                    }
                }
    }
}
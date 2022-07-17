package com.dxfeed.models

import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class QDService(private val logger: Logger) {
    suspend fun getLastQuoteByPromise(address: String, symbol: String, timeout: Long): Quote? = withContext(Dispatchers.IO) {
        DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createSubscription(Quote::class.java).use {
                        val promise = endpoint.feed
                                .getLastEventPromise(Quote::class.java, symbol)

                        if (!promise.awaitWithoutException(timeout, TimeUnit.SECONDS)) {
                            logger.log("QDService: getLastQuoteByPromise: timeout")

                            return@withContext null
                        }

                        return@withContext promise.result
                    }
                }
    }

    suspend fun testQuoteSubscription(address: String, symbols: List<String>, timeout: Long) = withContext(Dispatchers.IO) {
        logger.log("QDService: QuoteSub: Connecting")
        DXEndpoint.newBuilder()
                .build()
                .connect(address).use { endpoint ->
                    endpoint.feed.createSubscription(Quote::class.java).use { sub ->
                        sub.addEventListener { items ->
                            for (item in items) {
                                logger.log("QDService: QuoteSub: $item")
                            }
                        }
                        sub.addSymbols(symbols)

                        delay(timeout * 1000)

                        logger.log("QDService: QuoteSub: Disconnecting")
                    }
                }
    }
}
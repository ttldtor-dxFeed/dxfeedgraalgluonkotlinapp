package com.dxfeed.models

import com.dxfeed.api.DXEndpoint
import com.dxfeed.event.market.Quote
import java.util.concurrent.TimeUnit


class QDService(private val logger: Logger) {
    fun testQuoteLastEventPromise(address: String, symbol: String, timeout: Long): Quote? {
        val endpoint = DXEndpoint.newBuilder()
                .build()
                .connect(address)

        endpoint.feed.createSubscription(Quote::class.java).use {
            val promise = endpoint.feed
                    .getLastEventPromise(Quote::class.java, symbol)

            if (!promise.awaitWithoutException(timeout, TimeUnit.SECONDS)) {
                logger.log("QDService: testQuoteLastEventPromise -> timeout")

                return null
            }

            return promise.result
        }
    }
}
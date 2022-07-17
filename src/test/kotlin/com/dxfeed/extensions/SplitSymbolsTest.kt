package com.dxfeed.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SplitSymbolsTest {
    @Test
    fun testSplit() {
        assertIterableEquals("".splitSymbols(), listOf<String>())
        assertIterableEquals("AAPL".splitSymbols(), listOf("AAPL"))
        assertIterableEquals("AAPL,IBM".splitSymbols(), listOf("AAPL", "IBM"))
        assertIterableEquals("  AAPL   ,   IBM   ".splitSymbols(), listOf("AAPL", "IBM"))
        assertIterableEquals("   AAPL{=m,pl=0.1}  ,   IBM   ".splitSymbols(), listOf("AAPL{=m,pl=0.1}", "IBM"))
        assertIterableEquals(" AAPL&Q{=1d} , AAPL{=m,pl=0.1}  ,   IBM   ".splitSymbols(),
                listOf("AAPL&Q{=1d}", "AAPL{=m,pl=0.1}", "IBM"))
        assertIterableEquals(" IBM, AAPL&Q{=1d} , AAPL{=m,pl=0.1}  ,   IBM   ".splitSymbols(),
                listOf("IBM", "AAPL&Q{=1d}", "AAPL{=m,pl=0.1}"))
    }
}
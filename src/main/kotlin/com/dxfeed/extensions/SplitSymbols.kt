package com.dxfeed.extensions

fun String.splitSymbols(): List<String> {
    if (this.trim().isEmpty()) return listOf()

    val result = mutableListOf<String>()
    var curlyBraces = 0
    var s = ""

    for (i in 0 until this.length) {
        if (this[i] == '{') {
            curlyBraces++
            s += this[i]
        } else if (this[i] == ',' || this[i].isWhitespace()) {
            if (curlyBraces > 0) {
                s += this[i]
            } else if (this[i] == ',') {
                result.add(s)
                s = ""
            }
        } else if (this[i] == '}') {
            curlyBraces--
            s += this[i]
        } else {
            s += this[i]
        }
    }

    if (s.isNotEmpty()) {
        result.add(s)
    }

    return result.distinct()
}
package com.lq.ble_core.parse

data class ParseResult<T>(
    val value:T,
    val nextPosition:Int,
)
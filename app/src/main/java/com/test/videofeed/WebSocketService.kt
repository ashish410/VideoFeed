package com.test.videofeed

import org.json.JSONObject

interface WebSocketService {
    fun connect(socketUrl: String?)
    fun send(message: JSONObject?)
    fun cancel()
}
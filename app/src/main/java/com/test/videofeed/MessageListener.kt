package com.test.videofeed

import okio.ByteString
import org.json.JSONObject

interface MessageListener {
    fun onMessage(jsonObject: JSONObject?)
    fun onMessage(bytes: ByteString)
}
package com.test.videofeed

import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject

class WebSocketServiceImpl : WebSocketListener(), WebSocketService {

    private var ws: WebSocket? = null
    private var messageListener: MessageListener? = null

    private var okHttpClient: OkHttpClient = OkHttpClient()

    fun setMessageListener(messageListener: MessageListener?) {
        this.messageListener = messageListener
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "onOpen() : response : $response")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)

        messageListener?.onMessage(bytes)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d(TAG, "onMessage() : text : $text")
        try {
            val jsonObject = JSONObject(text)
            messageListener?.onMessage(jsonObject)
        } catch (e: JSONException) {
            Log.d(TAG, "onMessage() : JSONException : ${e.message}")
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "onClosing() : code : $code")
        Log.d(TAG, "onClosing() : reason : $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure() : Throwable : ${t.message}")
    }

    override fun connect(socketUrl:String?) {
        val request: Request = Request.Builder().url(socketUrl!!).build()
        ws = okHttpClient.newWebSocket(request, this)
        Log.d(TAG, "connect()")
    }

    override fun send(message: JSONObject?) {
        //TODO add room info while sending message
        val packet = message.toString()
        ws!!.send(packet)
        Log.d(TAG, "send() : message :$message")
    }

    override fun cancel() {
        ws!!.cancel()
        Log.d(TAG, "cancel()")
    }

    companion object {
        private const val TAG = "WebSocketServiceImpl"
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
package com.test.videofeed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import okio.ByteString
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity(), MessageListener {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var webSocketService: WebSocketServiceImpl
    private var streamUrl: String? = null

    companion object {
        const val TAG = "MainActivity"
        const val TEST_BASE64 = "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"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edt_url)
        button = findViewById(R.id.btn_go)
        imageView = findViewById(R.id.image)

        button.setOnClickListener {
            val text = editText.text.toString()
            text.let {
                streamUrl = text.trim()
                webSocketService.connect(streamUrl)
            }
        }

//        val dataByteArray: ByteArray = Base64.getDecoder().decode(TEST_BASE64)
//        val decodeSampledBitmapFromResource =
//            decodeSampledBitmapFromResource(dataByteArray, 200, 300)
//        imageView.setImageBitmap(decodeSampledBitmapFromResource)
    }

    override fun onResume() {
        super.onResume()

        webSocketService = WebSocketServiceImpl()
        webSocketService.setMessageListener(this)
        if (streamUrl.isNullOrEmpty()) return
        webSocketService.connect(streamUrl)
    }

    override fun onPause() {
        super.onPause()

        webSocketService.cancel()
    }

    override fun onMessage(bytes: ByteString) {
        val dataByteArray = bytes.toByteArray()

        val decodeSampledBitmapFromResource =
            decodeSampledBitmapFromResource(dataByteArray, 200, 300)
        imageView.setImageBitmap(decodeSampledBitmapFromResource)
    }

    override fun onMessage(jsonObject: JSONObject?) {
        Log.d(TAG, "json: $jsonObject")
        // { "data": "some-base-64-image-string" }
        jsonObject?.let {
            val byteArrayString: String = it.getString("data")
            val dataByteArray: ByteArray = byteArrayString.toByteArray()
            val decodeSampledBitmapFromResource =
                decodeSampledBitmapFromResource(dataByteArray, 200, 300)
            imageView.setImageBitmap(decodeSampledBitmapFromResource)
        }
    }

    fun decodeSampledBitmapFromResource(
        byteArray: ByteArray,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}
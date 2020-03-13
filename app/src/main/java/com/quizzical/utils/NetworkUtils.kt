package com.quizzical.utils

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkUtils {
    companion object {
        private val TAG: String = NetworkUtils::class.java.simpleName

        private val client = OkHttpClient()
        private val requestBuilder = Request.Builder()

        fun getRequest(url: String): String? {
            val request = requestBuilder.url(url).build()
            val response = client.newCall(request).execute()
            response.apply {
                if (isSuccessful) {
                    body()?.string()?.let {
                        return it
                    }
                    Log.e(
                        TAG,
                        "Call to $url successful. Could not parse body. Response code: ${code()}"
                    )
                    return null
                } else {
                    Log.e(TAG, "Call to $url failed. Response code: ${code()}")
                    return null
                }
            }
        }
    }
}
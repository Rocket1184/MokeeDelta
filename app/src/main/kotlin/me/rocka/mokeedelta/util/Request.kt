// MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-5

package me.rocka.mokeedelta.util

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.SocketTimeoutException

object Request {

    val client = OkHttpClient()

    fun get(url: String): String? {
        val req = Request.Builder()
                .url(url)
                .get()
                .build()
        try {
            val res = client.newCall(req).execute()
            return res.body().string()
        } catch (e: SocketTimeoutException) {

        } catch (e: IOException) {

        }
        return null
    }
}

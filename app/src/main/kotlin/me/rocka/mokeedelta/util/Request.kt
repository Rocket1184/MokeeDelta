// MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-5

package me.rocka.mokeedelta.util

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.SocketTimeoutException

object Request {

    val baseURL = "https://download.mokeedev.com"
    val linkPHP = "$baseURL/link.php"
    val client = OkHttpClient()

    fun deviceLink(device: String): String = "$baseURL/?device=$device"

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

    fun postKey(key: String): String? {
        val postBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .build()
        val req = Request.Builder()
                .url(linkPHP)
                .post(postBody)
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

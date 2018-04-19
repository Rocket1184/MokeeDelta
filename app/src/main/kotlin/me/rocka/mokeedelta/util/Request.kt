// MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-5

package me.rocka.mokeedelta.util

import me.rocka.mokeedelta.model.PostFilePayload
import me.rocka.mokeedelta.model.PostOtaPayload
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.SocketTimeoutException

object Request {

    private const val baseURL = "https://download.mokeedev.com"
    private const val fileURL = "$baseURL/file.php"
    private const val linkURL = "$baseURL/gen-link.php"
    private const val otaURL = "$baseURL/ota.php"
    private val client = OkHttpClient()
            .newBuilder()
            .cookieJar(SimpleCookieJar())
            .build()

    fun deviceLink(device: String): String = "$baseURL/?device=$device"

    fun get(url: String): String? {
        val req = Request.Builder()
                .url(url)
                .get()
                .build()
        try {
            val res = client.newCall(req).execute()
            return res.body()!!.string()
        } catch (e: SocketTimeoutException) {

        } catch (e: IOException) {

        }
        return null
    }

    fun postFile(params: PostFilePayload): String? {
        val postBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", params.key)
                .addFormDataPart("device", params.device)
                .addFormDataPart("type", params.type)
                .addFormDataPart("owner", params.owner)
                .build()
        val req = Request.Builder()
                .url(fileURL)
                .post(postBody)
                .build()
        try {
            val res = client.newCall(req).execute()
            return res.body()!!.string()
        } catch (e: SocketTimeoutException) {

        } catch (e: IOException) {

        }
        return null
    }

    fun postLink(key: String): String? {
        val postBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .build()
        val req = Request.Builder()
                .url(linkURL)
                .post(postBody)
                .build()
        try {
            val res = client.newCall(req).execute()
            return res.body()!!.string()
        } catch (e: SocketTimeoutException) {

        } catch (e: IOException) {

        }
        return null
    }

    fun postOta(params: PostOtaPayload): String? {
        val postBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("version", params.version)
                .addFormDataPart("owner", params.owner)
                .addFormDataPart("device", params.device)
                .addFormDataPart("type", params.type)
                .build()
        val req = Request.Builder()
                .url(otaURL)
                .post(postBody)
                .build()
        try {
            val res = client.newCall(req).execute()
            return res.body()!!.string()
        } catch (e: SocketTimeoutException) {

        } catch (e: IOException) {

        }
        return null
    }
}

// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-11-26

package me.rocka.mokeedelta.util

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class SimpleCookieJar : CookieJar {

    private var cookies: List<Cookie> = ArrayList<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        Log.d("Cookie", "Saving cookie $cookies")
        this.cookies = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        Log.d("Cookie", "Loading cookie $cookies")
        return cookies
    }
}
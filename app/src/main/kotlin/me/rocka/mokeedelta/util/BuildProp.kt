// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-8

package me.rocka.mokeedelta.util

import android.annotation.SuppressLint
import java.lang.reflect.InvocationTargetException

object BuildProp {

    @SuppressLint("PrivateApi")
    private val SystemProperties = Class.forName("android.os.SystemProperties")!!
    private val SystemProperties_get = SystemProperties.getDeclaredMethod("get", String::class.java)!!

    // rel: https://stackoverflow.com/a/39595278/8370777
    fun get(key: String): String? {
        try {
            return SystemProperties_get.invoke(null, key) as String
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }
}

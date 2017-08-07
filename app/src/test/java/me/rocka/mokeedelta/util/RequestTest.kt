package me.rocka.mokeedelta.util

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-7

class RequestTest {

    @Test
    fun get() {
        val res = Request.get(Request.deviceLink("cancro"))
        assertNotNull("Download page not null", res)
        assertTrue("Download page is HTML", res!!.startsWith("<!DOCTYPE html>"))
    }

    @Test
    fun postKey() {
        // key for `MK51.1-bacon-160420-HISTORY.zip`
        val testKey = "6dc44557d61bab352a3c052b8a134239"
        val res = Request.postKey(testKey)
        println(res)
        assertNotNull("Get link not null", res)
        assertTrue("Get link is http format", res!!.startsWith("http://rom.mk/"))
    }
}

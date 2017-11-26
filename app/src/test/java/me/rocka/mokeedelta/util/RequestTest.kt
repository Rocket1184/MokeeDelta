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
        val testKey = "519396dde5e71951a11b49862b3c4116"
        val res = Request.postKey(testKey)
        assertNotNull("Get link result not null", res)
        assertTrue("Get link result is HTML document", res!!.startsWith("<!DOCTYPE html>"))
    }

    @Test
    fun postUrl() {
        // Must get cookie before postUrl
        postKey()
        // url for `MK51.1-bacon-160420-HISTORY.zip`
        val testUrl = "gt5swn"
        val res = Request.postUrl(testUrl)
        assertNotNull("Post key result not null", res)
        assertTrue("Post key result is download link", res!!.startsWith("http://rom.mk/f/"))
        assertTrue("Post key result is reachable link", res.length > "http://rom.mk/f/".length)
    }
}

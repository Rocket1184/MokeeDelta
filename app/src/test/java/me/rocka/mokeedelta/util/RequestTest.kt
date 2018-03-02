package me.rocka.mokeedelta.util

import me.rocka.mokeedelta.model.PostOtaPayload
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
        assertTrue("There are some devices in HTML Doc", Parser.parseDevices(res).isNotEmpty())
    }

    @Test
    fun postFile() {
        // key for `MK51.1-bacon-160420-HISTORY.zip`
        val testKey = "519396dde5e71951a11b49862b3c4116"
        val res = Request.postFile(testKey)
        assertNotNull("Get link result not null", res)
        assertTrue("Get link result is HTML document", res!!.startsWith("<!DOCTYPE html>"))
        assertTrue("There is a key in HTML Doc", Parser.parseRealKey(res)!!.isNotEmpty())
    }

    @Test
    fun postLink() {
        // Must get cookie before postLink
        postFile()
        // url for `MK51.1-bacon-160420-HISTORY.zip`
        val testUrl = "kx5yt3"
        val res = Request.postLink(testUrl)
        assertNotNull("Post key result not null", res)
        assertTrue("Post key result is a link", res!!.startsWith("http://rom.mk/"))
        assertTrue("Post key result is a reachable link", res.length > "http://rom.mk/".length)
    }

    @Test
    fun postOta() {
        // Must get cookie before postOta
        postFile()
        // javascript:downloadPost('/ota.php', {version:'MK60.1-bacon-170413-RELEASE', owner:'official', device:'bacon', type:'release'})
        val payload = PostOtaPayload(
                version = "MK60.1-bacon-170413-RELEASE",
                owner = "official",
                device = "bacon",
                type = "release"
        )
        val res = Request.postOta(payload)
        assertNotNull("Get link result not null", res)
        assertTrue("Get link result is HTML document", res!!.startsWith("<!DOCTYPE html>"))
        assertTrue("Have at least one package", Parser.parseDeltaPkg(res).isNotEmpty())
    }
}

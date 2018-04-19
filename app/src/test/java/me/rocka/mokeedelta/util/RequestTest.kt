package me.rocka.mokeedelta.util

import me.rocka.mokeedelta.model.PostFilePayload
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
        // <td><a href="javascript:void(0);" onclick="javascript:downloadPost('/file.php', {key:'257e538ce3ae7f20b8f1abec8c2e968b', device:'bacon', type:'history', owner:''})" id="tdurl">MK51.1-bacon-160420-HISTORY.zip</a><br><small>md5sum: 9a51de8041bccccdfb7f7af61f6024dd</small></td>
        val payload = PostFilePayload(
                key = "257e538ce3ae7f20b8f1abec8c2e968b",
                device = "bacon",
                type = "history",
                owner = ""
        )
        val res = Request.postFile(payload)
        assertNotNull("Get link result not null", res)
        assertTrue("Get link result is HTML document", res!!.startsWith("<!DOCTYPE html>"))
        assertTrue("There is a key in HTML Doc", Parser.parseRealKey(res)!!.isNotEmpty())
        assertNotNull("There are delta params in HTML Doc", Parser.parsePostPayload(res))
    }

    @Test
    fun postLink() {
        // Must get cookie before postLink
        postFile()
        // key for `MK51.1-bacon-160420-HISTORY.zip`
        val key = "8dea9f55a5c1340db6ec69add36e79a9"
        val res = Request.postLink(key)
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

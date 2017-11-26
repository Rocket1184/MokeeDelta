package me.rocka.mokeedelta.util

import org.junit.Assert.*
import org.junit.Test

// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-7

class ParserTest {

    val deviceHtml = """
        <li id="device_bacon">
            <a href="javascript:void(0)" onclick="navigate_device('bacon');"><span>1 (bacon)</span></a>
        </li>
        <li id="device_cheeseburger">
            <a href="javascript:void(0)" onclick="navigate_device('cheeseburger');"><span>5 (cheeseburger)</span></a>
        </li>
        <li id="device_onyx">
            <a href="javascript:void(0)" onclick="navigate_device('onyx');"><span>X (onyx)</span></a>
        </li>
        <li id="device_oneplus3">
            <a href="javascript:void(0)" onclick="navigate_device('oneplus3');"><span>3/3T (3T unsupported unofficial and 6.0) (oneplus3)</span></a>
        </li>
        <li id="device_tocino">
            <a href="javascript:void(0)" onclick="navigate_device('tocino');"><span>2 (tocino)</span></a>
        </li>"""
    val fullPkgHtml = """
        <tr>
            <td><a href="/?device=bacon">bacon</a><br/><small class="md5">OnePlus 1</small></td>
            <td>每夜版</td>
            <td><a href="javascript:void(0);" onclick="javascript:downloadPost('download.php', {key:'97f99da25404f9865d487a123dadcfb3'})" id="tdurl">MK71.2-bacon-201708060312-NIGHTLY.zip</a><br/><small>md5sum: ae8b83e35949e6a5af23002adccb0543&nbsp;<a href="ota.php?version=MK71.2-bacon-201708060312-NIGHTLY&owner=">增量更新</a></small></td>
            <td>405.13 MB</td>
            <td>140</td>
            <td>2017-08-06 04:31:36</td>
        </tr>
"""
    val deltaPkgHtml = """
        <tr>
            <td><a href="javascript:void(0);" onclick="javascript:downloadPost('download.php', {key:'6a2dca68f5d265054e95c0b9b079aa9c'})" id="tdurl">OTA-MK71.2-cancro-201708050418-201708060418-NIGHTLY.zip</a><br/><small>md5sum: 11b82c8bf6340b5dc3f8e7e271e6d99f</small></td>
            <td>26.76 MB</td>
            <td>89</td>
            <td>2017-08-06 05:34:56</td>
        </tr>"""
    val versionStr = "MK71.2-bacon-201708060312-NIGHTLY"
    val realKeyHtml = """
<script language=javascript>var wait="60"
var secs=0;for(var i=1;i<=wait;i++){setTimeout("sTimer("+i+")",i*1000);}function genLink(){if(document.readyState=="complete"&&window.adVerified==true){${'$'}.post("gen-link.php",{url:"fxung1"},function(data,status){${'$'}('#alert-success-enabled').html('<a href="'+data+'" class="alert-link">点击下载 MK71.2-bacon-201711260154-NIGHTLY.zip</a>');});}else{${'$'}('#alert-danger-enabled').show();${'$'}('#alert-success-enabled').hide();setTimeout("genLink()",1000);}}function sTimer(num){if(num!=wait){secs=wait-num;${'$'}('#Timer').html(secs);}else{genLink();}}window.setTimeout(function(){if(adsbygoogle instanceof Array){${'$'}('#alert-danger-enabled').show();${'$'}('#alert-success-enabled').hide();}},5000);window.setTimeout(function(){function Script(callback){var js=document.createElement("script");this.js=js;js.setAttribute("type",'text/javascript');var head=document.getElementsByTagName('head')[0];head.appendChild(js);if(navigator.appName.toLowerCase().indexOf('netscape')==-1){js.onreadystatechange=function(){console.log('test',js.status);if(js.readyState=='complete'){callback(js);}}}else{js.onload=function(){callback(js);}}}Script.prototype.get=function(url){this.js.src=url;}
function script_onload(script){window.adVerified=true;}var load_js=new Script(script_onload);load_js.get('https://adservice.google.com/adsid/integrator.js?domain=download.mokeedev.com');},10000);</script>
    """

    @Test
    fun parseCurrentVersion() {
        val current = Parser.parseCurrentVersion(versionStr)
        assertEquals("Device equals `bacon`", "bacon", current.device)
        assertEquals("Version equals `201708060312`", "201708060312", current.version)
        assertEquals("Channel equals `NIGHTLY`", "NIGHTLY", current.channel.toString())
    }

    @Test
    fun parseDevices() {
        val devices = Parser.parseDevices(deviceHtml)
        assertNotEquals("Device list not empty", 0, devices)
        assertEquals("There should be 5 devices", 5, devices.size)
        println("device\t\t\tmodel")
        devices.forEach {
            assertNotNull("Device model not null", it.model)
            assertNotNull("Device name not null", it.device)
            println("${it.device}\t\t\t${it.model}")
        }
    }

    @Test
    fun parseFullPkg() {
        val pkgList = Parser.parseFullPkg(fullPkgHtml)
        assertNotEquals("Full package list not empty", 0, pkgList.size)
        assertEquals("Full package list should contain 1 item", 1, pkgList.size)
        assertEquals("Device equals `bacon`", "bacon", pkgList[0].device)
        assertEquals("Version equals `201708060312`", "201708060312", pkgList[0].version)
        assertEquals("Channel equals `NIGHTLY`", "NIGHTLY", pkgList[0].channel.toString())
        assertEquals("Size equals `405.13 MB`", "405.13 MB", pkgList[0].size)
        assertEquals("Key equals ...", "97f99da25404f9865d487a123dadcfb3", pkgList[0].key)
        assertEquals("md5sum equals ...", "ae8b83e35949e6a5af23002adccb0543", pkgList[0].md5sum)
        assertEquals("deltaUrl equals ...", "${Parser.deltaRoot}ota.php?version=MK71.2-bacon-201708060312-NIGHTLY&owner=", pkgList[0].deltaUrl)
    }

    @Test
    fun parseDeltaPkg() {
        val pkgList = Parser.parseDeltaPkg(deltaPkgHtml)
        assertNotEquals("Delta package list not empty", 0, pkgList.size)
        assertEquals("Delta package list should contain 1 item", 1, pkgList.size)
        assertEquals("Device equals `cancro`", "cancro", pkgList[0].device)
        assertEquals("Base equals `201708050418`", "201708050418", pkgList[0].base)
        assertEquals("Target equals `201708060418`", "201708060418", pkgList[0].target)
        assertEquals("Size equals `26.76 MB`", "26.76 MB", pkgList[0].size)
        assertEquals("Key equals ...", "6a2dca68f5d265054e95c0b9b079aa9c", pkgList[0].key)
        assertEquals("md5sum equals ...", "11b82c8bf6340b5dc3f8e7e271e6d99f", pkgList[0].md5sum)
    }

    @Test
    fun parseRealKey() {
        val realKey = Parser.parseRealKey(realKeyHtml)
        assertNotNull("Real key (url) not null", realKey)
        assertEquals("Real key (url) equals `fxung1`", "fxung1", realKey)
    }
}

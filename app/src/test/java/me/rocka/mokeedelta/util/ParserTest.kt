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
          <td>正式版</td>
          <td><a href="javascript:void(0);" onclick="javascript:downloadPost('download.php', {key:'168fc748f497af801cac14d36d6ac78f'})" id="tdurl">MK60.1-bacon-170413-RELEASE.zip</a><br/><small>md5sum: d27d1c77d4b806d8833a5994730c7146&nbsp;<a href="javascript:void(0);" onclick="location.href='ota.php?version=MK60.1-bacon-170413-RELEASE&owner='">增量更新</a></small></td>
          <td>414.95 MB</td>
          <td>4059</td>
          <td>2017-04-13 19:49:46</td>
        </tr>"""
    val deltaPkgHtml = """
        <tr>
          <td><a href="javascript:void(0);" onclick="javascript:downloadPost('download.php', {key:'9ce9bc3d0e11364a371df9979a03e667'})" id="tdurl">OTA-MK71.2-cancro-201711210256-201711220341-NIGHTLY.zip</a><br/><small>md5sum: 3a56cbbb706616026ef15614aa430c2d</small></td>
          <td>12.8 MB</td>
          <td>16</td>
          <td>2017-11-23 07:14:01</td>
        </tr>"""
    val versionStr = "MK71.2-bacon-201708060312-NIGHTLY"
    val realKeyHtml = """
<script language=javascript>var wait="60"
var secs=0;for(var i=1;i<=wait;i++){setTimeout("sTimer("+i+")",i*1000);}function genLink(){if(document.readyState=="complete"&&window.adVerified==true){${'$'}.post("gen-link.php",{url:"fxung1"},function(data,status){${'$'}('#alert-success-enabled').html('<a href="'+data+'" class="alert-link">点击下载 MK71.2-bacon-201711260154-NIGHTLY.zip</a>');});}else{${'$'}('#alert-danger-enabled').show();${'$'}('#alert-success-enabled').hide();setTimeout("genLink()",1000);}}function sTimer(num){if(num!=wait){secs=wait-num;${'$'}('#Timer').html(secs);}else{genLink();}}window.setTimeout(function(){if(adsbygoogle instanceof Array){${'$'}('#alert-danger-enabled').show();${'$'}('#alert-success-enabled').hide();}},5000);window.setTimeout(function(){function Script(callback){var js=document.createElement("script");this.js=js;js.setAttribute("type",'text/javascript');var head=document.getElementsByTagName('head')[0];head.appendChild(js);if(navigator.appName.toLowerCase().indexOf('netscape')==-1){js.onreadystatechange=function(){console.log('test',js.status);if(js.readyState=='complete'){callback(js);}}}else{js.onload=function(){callback(js);}}}Script.prototype.get=function(url){this.js.src=url;}
function script_onload(script){window.adVerified=true;}var load_js=new Script(script_onload);load_js.get('https://adservice.google.com/adsid/integrator.js?domain=download.mokeedev.com');},10000);</script>"""

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
        assertEquals("Version equals `170413`", "170413", pkgList[0].version)
        assertEquals("Channel equals `RELEASE`", "RELEASE", pkgList[0].channel.toString())
        assertEquals("Size equals `414.95 MB`", "414.95 MB", pkgList[0].size)
        assertEquals("Key equals ...", "168fc748f497af801cac14d36d6ac78f", pkgList[0].key)
        assertEquals("md5sum equals ...", "d27d1c77d4b806d8833a5994730c7146", pkgList[0].md5sum)
        assertEquals("deltaUrl equals ...", "${Parser.deltaRoot}ota.php?version=MK60.1-bacon-170413-RELEASE&owner=", pkgList[0].deltaUrl)
    }

    @Test
    fun parseDeltaPkg() {
        val pkgList = Parser.parseDeltaPkg(deltaPkgHtml)
        assertNotEquals("Delta package list not empty", 0, pkgList.size)
        assertEquals("Delta package list should contain 1 item", 1, pkgList.size)
        assertEquals("Device equals `cancro`", "cancro", pkgList[0].device)
        assertEquals("Base equals `201711210256`", "201711210256", pkgList[0].base)
        assertEquals("Target equals `201711220341`", "201711220341", pkgList[0].target)
        assertEquals("Size equals `12.8 MB`", "12.8 MB", pkgList[0].size)
        assertEquals("Key equals ...", "9ce9bc3d0e11364a371df9979a03e667", pkgList[0].key)
        assertEquals("md5sum equals ...", "3a56cbbb706616026ef15614aa430c2d", pkgList[0].md5sum)
    }

    @Test
    fun parseRealKey() {
        val realKey = Parser.parseRealKey(realKeyHtml)
        assertNotNull("Real key (url) not null", realKey)
        assertEquals("Real key (url) equals `fxung1`", "fxung1", realKey)
    }
}

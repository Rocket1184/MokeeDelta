package me.rocka.mokeedelta.util

import org.junit.Assert.*
import org.junit.Test

// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-7

class ParserTest {

    private val deviceHtml = """
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
    private val fullPkgHtml = """
        <tr>
          <td><a href="/?device=bacon">bacon</a><br/><small class="md5">OnePlus 1</small></td>
          <td>正式版</td>
          <td><a href="javascript:void(0);" onclick="javascript:downloadPost('download.php', {key:'168fc748f497af801cac14d36d6ac78f'})" id="tdurl">MK60.1-bacon-170413-RELEASE.zip</a><br/><small>md5sum: d27d1c77d4b806d8833a5994730c7146&nbsp;<a href="javascript:void(0);" onclick="location.href='ota.php?version=MK60.1-bacon-170413-RELEASE&owner='">增量更新</a></small></td>
          <td>414.95 MB</td>
          <td>4059</td>
          <td>2017-04-13 19:49:46</td>
        </tr>"""
    private val deltaPkgHtml = """
        <tr>
          <td><a href="javascript:void(0);" onclick="javascript:downloadPost('download.php', {key:'9ce9bc3d0e11364a371df9979a03e667'})" id="tdurl">OTA-MK71.2-cancro-201711210256-201711220341-NIGHTLY.zip</a><br/><small>md5sum: 3a56cbbb706616026ef15614aa430c2d</small></td>
          <td>12.8 MB</td>
          <td>16</td>
          <td>2017-11-23 07:14:01</td>
        </tr>"""
    private val versionStr = "MK71.2-bacon-201708060312-NIGHTLY"
    private val realKeyHtml = """function genLink() {
                  if (document.readyState == "complete" && window.adVerified == true) {
                      ${'$'}.post("gen-link.php",
                      {
                          url:"m5tn2i"
                      },
                      function(data, status) {
                          ${'$'}('#alert-success-enabled').html('<a href="' + data + '" class="alert-link">点击下载 OTA-MK71.2-bacon-201712220041-201712230040-NIGHTLY.zip</a>');
                      });
                  } else {
                      ${'$'}('#alert-danger-enabled').show();
                      ${'$'}('#alert-success-enabled').hide();
                      setTimeout("genLink()", 1000);
                  }
              }"""

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
        assertEquals("Real key (url) equals `m5tn2i`", "m5tn2i", realKey)
    }
}

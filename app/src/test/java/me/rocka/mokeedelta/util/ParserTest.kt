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
        <li id="device_dumpling">
            <a href="javascript:void(0)" onclick="navigate_device('dumpling');"><span>5T (dumpling)</span></a>
        </li>
        <li id="device_onyx">
            <a href="javascript:void(0)" onclick="navigate_device('onyx');"><span>X (onyx)</span></a>
        </li>
        <li id="device_oneplus3">
            <a href="javascript:void(0)" onclick="navigate_device('oneplus3');"><span>3/3T (3T unsupported 6.0) (oneplus3)</span></a>
        </li>
        <li id="device_tocino">
            <a href="javascript:void(0)" onclick="navigate_device('tocino');"><span>2 (tocino)</span></a>
        </li>"""
    private val fullPkgHtml = """
        <tr>
          <td><a href="/?device=bacon">bacon</a><br/><small class="md5">OnePlus 1</small></td>
          <td>正式版</td>
          <td><a href="javascript:void(0);" onclick="javascript:downloadPost('/file.php', {key:'73c3e6618a4b75c3431e67da712df6a5', device:'bacon', type:'release', owner:''})" id="tdurl">MK60.1-bacon-170413-RELEASE.zip</a><br/><small>md5sum: d27d1c77d4b806d8833a5994730c7146</small></td>
          <td>414.95 MB</td>
          <td>2017-04-13 19:49:46</td>
        </tr>"""
    private val deltaPkgHtml = """
        <tr>
          <td><a href="javascript:void(0);" onclick="javascript:downloadPost('/file.php', {key:'7509a40d1eec09a45f19f01f4d686fec', device:'bacon', type:'release'})" id="tdurl">OTA-MK60.1-bacon-170408-170413-RELEASE.zip</a><br/><small>md5sum: 2f33b66a269e6554922d45e0f0349ce9</small></td>
          <td>11.08 MB</td>
          <td>2017-04-13 21:37:46</td>
        </tr>"""
    private val versionStr = "MK71.2-bacon-201708060312-NIGHTLY"
    private val realKeyHtml = """
        function genLink() {
            $.post("/gen-link.php",
            {
                url:"ra1jrz"
            },
            function(data, status) {
                $('#alert-success-enabled').html('<a href="' + data + '" class="alert-link">点击下载 MK71.2-bacon-201803020045-NIGHTLY.zip</a>');
            });
        }"""
    private val deltaPayloadHtml = """
        <div class="card-content green tip-text-green" id="alert-success-enabled">
          <span class="white-text">为了节约成本和资源，保障所有用户的正常下载，我们限制了完整包的下载速度。若要高速下载，请在魔趣中心捐赠我们并通过该应用下载更新。<a href="http://bbs.mokeedev.com/t/topic/25" target="_blank">查看公告</a><br/>正在准备资源，请等待 <span id="Timer">60</span> 秒或下载适用您当前版本的增量更新快速升级（不限制下载速度）。<a href="javascript:void(0); "onclick="javascript:downloadPost('/ota.php', {version:'MK60.1-bacon-170413-RELEASE', owner:'official', device:'bacon', type:'release'})">立即下载增量更新！</a></span>
        </div>
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
        assertEquals("There should be 6 devices", 6, devices.size)
        devices.forEach {
            assertNotNull("Device model not null", it.model)
            assertNotNull("Device name not null", it.device)
            println(it)
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
        assertEquals("Key equals ...", "73c3e6618a4b75c3431e67da712df6a5", pkgList[0].key)
        assertEquals("md5sum equals ...", "d27d1c77d4b806d8833a5994730c7146", pkgList[0].md5sum)
        assertEquals("Date equals ...", "2017-04-13 19:49:46", pkgList[0].date)
    }

    @Test
    fun parseDeltaPkg() {
        val pkgList = Parser.parseDeltaPkg(deltaPkgHtml)
        assertNotEquals("Delta package list not empty", 0, pkgList.size)
        assertEquals("Delta package list should contain 1 item", 1, pkgList.size)
        assertEquals("Device equals `bacon`", "bacon", pkgList[0].device)
        assertEquals("Base equals `170408`", "170408", pkgList[0].base)
        assertEquals("Target equals `170413`", "170413", pkgList[0].target)
        assertEquals("Size equals `11.08 MB`", "11.08 MB", pkgList[0].size)
        assertEquals("Key equals ...", "7509a40d1eec09a45f19f01f4d686fec", pkgList[0].key)
        assertEquals("md5sum equals ...", "2f33b66a269e6554922d45e0f0349ce9", pkgList[0].md5sum)
        assertEquals("Date equals ...", "2017-04-13 21:37:46", pkgList[0].date)
    }

    @Test
    fun parseRealKey() {
        val realKey = Parser.parseRealKey(realKeyHtml)
        assertNotNull("Real key (url) not null", realKey)
        assertEquals("Real key (url) equals `ra1jrz`", "ra1jrz", realKey)
    }

    @Test
    fun parseDeltaPayload() {
        val payload = Parser.parseDeltaPayload(deltaPayloadHtml)
        assertEquals("version equals ...", "MK60.1-bacon-170413-RELEASE", payload.version)
        assertEquals("owner equals ...", "official", payload.owner)
        assertEquals("device equals ...", "bacon", payload.device)
        assertEquals("type equals ...", "release", payload.type)
    }
}

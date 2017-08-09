// MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-6

package me.rocka.mokeedelta.util

import me.rocka.mokeedelta.model.DeltaPackage
import me.rocka.mokeedelta.model.Device
import me.rocka.mokeedelta.model.FullPackage
import me.rocka.mokeedelta.model.ReleaseChannel

object Parser {

    val deltaRoot = "https://download.mokeedev.com/"
    val deviceRe = Regex("""<li id="device_([^"]+)">[^<]+[^>]+><span>([^ ]+)[^<]+</span></a>""")
    val fullPkgRe = Regex("""<td><a href="javascript:downloadPost\('download.php', ?\{key:'([^']+)'[^>]+>([^<]+)</a?><br ?/><small>md5sum: ([^&]+)&nbsp;<a href="([^"]+)">[^<]+</a></small></td>[^<]+<td>([^<]+)</td>""")
    val deltaPkgRe = Regex("""<tr>[^<]+<td><a href="javascript:downloadPost\('download.php', ?\{key:'([^']+)'[^>]+>([^<]+)</a><br/><small>md5sum: ([^<]+)</small></td>[^<]+<td>([^<]+)</td>""")
    val fullPkgFilenameRe = Regex("""(MK\d+\.\d+)-([^-]+)-(\d+)-(\w+)""")
    val deltaPkgFilenameRe = Regex("""OTA-(MK\d+\.\d+)-([^-]+)-(\d+)-(\d+)-(\w+)""")

    fun parseDevices(input: String): List<Device> {
        val result = deviceRe.findAll(input)
        val output = ArrayList<Device>()
        result.forEach {
            output.add(Device(it.groupValues[1], it.groupValues[2]))
        }
        return output
    }

    fun parseCurrentVersion(input: String): FullPackage {
        val result = fullPkgFilenameRe.find(input)
        return FullPackage(
                device = result?.groupValues?.get(2) ?: "",
                model = "",
                fileName = "$input.zip",
                md5sum = "",
                deltaUrl = "",
                key = "",
                version = result?.groupValues?.get(3) ?: "",
                size = "",
                url = "",
                channel = ReleaseChannel.valueOf(result?.groupValues?.get(4) ?: ReleaseChannel.UNKNOWN.toString())
        )
    }

    fun parseFullPkg(input: String): List<FullPackage> {
        val result = fullPkgRe.findAll(input)
        val output = ArrayList<FullPackage>()
        result.forEach {
            val versionResult = fullPkgFilenameRe.find(it.groupValues[2])
            output.add(FullPackage(
                    device = versionResult!!.groupValues[2],
                    model = "",
                    fileName = it.groupValues[2],
                    md5sum = it.groupValues[3],
                    deltaUrl = "$deltaRoot${it.groupValues[4]}",
                    key = it.groupValues[1],
                    version = versionResult.groupValues[3],
                    size = it.groupValues[5],
                    url = "",
                    channel = ReleaseChannel.valueOf(versionResult.groupValues[4])
            ))
        }
        return output
    }

    fun parseDeltaPkg(input: String): List<DeltaPackage> {
        val result = deltaPkgRe.findAll(input)
        val output = ArrayList<DeltaPackage>()
        result.forEach {
            val versionResult = deltaPkgFilenameRe.find(it.groupValues[2])
            output.add(DeltaPackage(
                    device = versionResult!!.groupValues[2],
                    model = "",
                    fileName = it.groupValues[2],
                    md5sum = it.groupValues[3],
                    key = it.groupValues[1],
                    size = it.groupValues[4],
                    url = "",
                    base = versionResult.groupValues[3],
                    target = versionResult.groupValues[4]
            ))
        }
        return output
    }
}

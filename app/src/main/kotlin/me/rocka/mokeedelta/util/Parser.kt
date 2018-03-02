// MokeeDelta/me.rocka.mokeedelta.util
// rocka, 17-8-6

package me.rocka.mokeedelta.util

import me.rocka.mokeedelta.model.*

object Parser {

    private val deviceRe = Regex("""<li id="device_([^"]+)">[^<]+[^>]+><span>([^ ]+)[^<]+</span></a>""")
    private val fullPkgRe = Regex("""<td><a href="javascript:void\(0\);" onclick="javascript:downloadPost\('/file.php', ?\{key:'([^']+)', ?device:'([^']+)', ?type:'([^']+)', ?owner:'([^']+)?'}\)" id="tdurl">([^<]+)</a><br ?/?><small>md5sum: ([^&]+)</small></td>[^<]+<td>([^<]+)</td>[^<]+<td>([^<]+)</td>""")
    private val deltaPkgRe = Regex("""<td><a href="javascript:void\(0\);" onclick="javascript:downloadPost\('/file.php', ?\{key:'([^']+)', ?device:'([^']+)', ?type:'([^']+)'}\)" id="tdurl">([^<]+)</a><br ?/?><small>md5sum: ([^&]+)</small></td>[^<]+<td>([^<]+)</td>[^<]+<td>([^<]+)</td>""")
    private val deltaPayloadRe = Regex("""<a href="javascript:void\(0\); "onclick="javascript:downloadPost\('/ota.php', ?\{version:'([^']+)', owner:'([^']+)', device:'([^']+)', type:'([^']+)'}\)">[^<]+</a></span>""")
    private val realKeyRe = Regex(""""?url"?: *"([^"]+)""")
    private val fullPkgFilenameRe = Regex("""(MK\d+\.\d+)-([^-]+)-(\d+)-(\w+)""")
    private val deltaPkgFilenameRe = Regex("""OTA-(MK\d+\.\d+)-([^-]+)-(\d+)-(\d+)-(\w+)""")

    fun parseDevices(input: String): List<Device> {
        val result = deviceRe.findAll(input)
        val output = ArrayList<Device>()
        result.forEach {
            output.add(Device(it.groupValues[1], it.groupValues[2]))
        }
        return output
    }

    fun parseRealKey(input: String): String? {
        val result = realKeyRe.findAll(input)
        return result.firstOrNull()?.groupValues?.get(1)
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
                channel = ReleaseChannel.valueOf(result?.groupValues?.get(4)
                        ?: ReleaseChannel.UNKNOWN.toString()),
                date = ""
        )
    }

    fun parseFullPkg(input: String): List<FullPackage> {
        val result = fullPkgRe.findAll(input)
        val output = ArrayList<FullPackage>()
        result.forEach {
            val versionResult = fullPkgFilenameRe.find(it.groupValues[5])
            output.add(FullPackage(
                    device = versionResult!!.groupValues[2],
                    model = "",
                    fileName = it.groupValues[5],
                    md5sum = it.groupValues[6],
                    deltaUrl = "",
                    key = it.groupValues[1],
                    version = versionResult.groupValues[3],
                    size = it.groupValues[7],
                    url = "",
                    channel = ReleaseChannel.valueOf(versionResult.groupValues[4]),
                    date = it.groupValues[8]
            ))
        }
        return output
    }

    fun parseDeltaPkg(input: String): List<DeltaPackage> {
        val result = deltaPkgRe.findAll(input)
        val output = ArrayList<DeltaPackage>()
        result.forEach {
            val versionResult = deltaPkgFilenameRe.find(it.groupValues[4])
            output.add(DeltaPackage(
                    device = versionResult!!.groupValues[2],
                    model = "",
                    fileName = it.groupValues[4],
                    md5sum = it.groupValues[5],
                    key = it.groupValues[1],
                    size = it.groupValues[6],
                    url = "",
                    base = versionResult.groupValues[3],
                    target = versionResult.groupValues[4],
                    date = it.groupValues[7]
            ))
        }
        return output
    }

    fun parseDeltaPayload(input: String): PostOtaPayload {
        val result = deltaPayloadRe.find(input)!!
        return PostOtaPayload(
                version = result.groupValues[1],
                owner = result.groupValues[2],
                device = result.groupValues[3],
                type = result.groupValues[4]
        )
    }
}

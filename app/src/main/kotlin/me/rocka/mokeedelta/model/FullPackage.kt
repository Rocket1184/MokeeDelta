// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.model
// rocka, 17-8-6

package me.rocka.mokeedelta.model

data class FullPackage(
        // Build.DEVICE
        val device: String,
        // Build.MODEL
        override val model: String,
        val fileName: String,
        val md5sum: String,
        override val size: String,
        val url: String,
        val version: String,
        val deltaUrl: String,
        override val key: String,
        val channel: ReleaseChannel,
        override val date: String
) : IRomPackage {
    override val name: String
        get() = "$version-$channel"
}

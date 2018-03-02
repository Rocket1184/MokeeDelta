// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.model
// rocka, 17-8-6

package me.rocka.mokeedelta.model

data class FullPackage(
        override val device: String,
        override val model: String,
        override val size: String,
        override val key: String,
        override val type: String,
        override val date: String,
        override val owner: String,
        override val md5sum: String,
        override val fileName: String,
        val version: String,
        val deltaUrl: String,
        val channel: ReleaseChannel
) : IRomPackage {
    override val name: String
        get() = "$version-$channel"
}

// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.model
// rocka, 17-8-5

package me.rocka.mokeedelta.model

data class DeltaPackage(
        // Build.DEVICE
        val device: String,
        // Build.MODEL
        override val model: String,
        val fileName: String,
        val md5sum: String,
        override val size: String,
        val url: String,
        val base: String,
        val target: String,
        override val key: String,
        override val type: String,
        override val date: String
) : IRomPackage {
    override val name: String
        get() = target
}

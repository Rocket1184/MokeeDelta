// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.model
// rocka, 17-8-5

package me.rocka.mokeedelta.model

data class DeltaPackage(
        override val device: String,
        override val model: String,
        override val size: String,
        override val key: String,
        override val type: String,
        override val date: String,
        override val owner: String,
        override val md5sum: String,
        override val fileName: String,
        val base: String,
        val target: String
) : IRomPackage {
    override val name: String
        get() = target
}

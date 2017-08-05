// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.model
// rocka, 17-8-5

package me.rocka.mokeedelta.model

data class DeltaPackage(
        // Build.DEVICE
        val device: String,
        // Build.MODEL
        val model: String,
        val fileName: String,
        val md5sum: String,
        val size: String,
        val url: String,
        val version: String,
        val base: String,
        val target: String,
        val key: String
)

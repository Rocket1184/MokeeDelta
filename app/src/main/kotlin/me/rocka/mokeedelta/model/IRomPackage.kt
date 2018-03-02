// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.model
// rocka, 17-8-5

package me.rocka.mokeedelta.model

interface IRomPackage {
    // Build.DEVICE
    val device: String
    // Build.MODEL
    val model: String
    val name: String
    val size: String
    val key: String
    val type: String
    val date: String
    val owner: String
    val md5sum: String
    val fileName: String
}

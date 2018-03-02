// MokeeDelta/me.rocka.mokeedelta.util
// rocka, 18-3-2

package me.rocka.mokeedelta.model

data class PostOtaPayload(
        val version: String,
        val owner: String,
        val device: String,
        val type: String
)

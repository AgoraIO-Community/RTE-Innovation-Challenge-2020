package com.qifan.emojibattle.model

import androidx.annotation.DrawableRes

data class Emoji(
    @DrawableRes val battleRes: Int,
    val state: State = State.UNKNOWN,
    val verified: Boolean = false
) {
    enum class State { SMILE, LEFT_WINK, RIGHT_WINK, UNKNOWN }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Emoji
        if (other.battleRes != battleRes) return false
        if (other.state != state) return false
        if (other.verified != verified) return false
        return true
    }

    override fun hashCode(): Int {
        var result = battleRes.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + verified.hashCode()
        return result
    }
}
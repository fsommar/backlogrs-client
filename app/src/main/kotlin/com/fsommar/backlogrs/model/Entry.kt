package com.fsommar.backlogrs.model

import java.util.Date
import java.io.Serializable

/**
 * Created by Fredrik Sommar on 2015-02-27.
 */
public data class Entry(
        val id: Int? = null,
        val game_id: Int? = null,
        val time_played: Float? = null,
        val last_update: Date? = null,
        val status: Status? = null,
        val game: Game? = null) : Serializable {
}
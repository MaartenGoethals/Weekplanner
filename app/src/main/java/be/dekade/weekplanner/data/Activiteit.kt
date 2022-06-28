package be.dekade.weekplanner.data

import java.sql.Time
import java.time.Duration

data class Activiteit(
    val titel: String,
    val notities: String,
    val startuur: Int,
    val startminuut: Int,
    val duurMinuten: Int,
    val isMaandag: Boolean,
    val isDinsdag: Boolean,
    val isWoensdag: Boolean,
    val isDonderdag: Boolean,
    val isVrijdag: Boolean,
    val isZaterdag: Boolean,
    val isZondag: Boolean,
    val uitstelUur: Int,
    val uitstelMinuut: Int,
    val isNotificatieAan: Boolean
)
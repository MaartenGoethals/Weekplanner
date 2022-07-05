package be.dekade.weekplanner.data

import java.sql.Time
import java.time.Duration

data class Activiteit(
    val id: Int,
    var titel: String,
    var notities: String,
    var startuur: Int,
    var startminuut: Int,
    var isMaandag: Boolean,
    var isDinsdag: Boolean,
    var isWoensdag: Boolean,
    var isDonderdag: Boolean,
    var isVrijdag: Boolean,
    var isZaterdag: Boolean,
    var isZondag: Boolean,
    var uitstelUur: Int,
    var uitstelMinuut: Int,
    var isNotificatieAan: Boolean,
    var isAfgewerkt: Boolean
)
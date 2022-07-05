package be.dekade.weekplanner.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.Duration

@Entity(tableName = "activiteiten")
data class Activiteit(
    @PrimaryKey @ColumnInfo(name = "id")
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
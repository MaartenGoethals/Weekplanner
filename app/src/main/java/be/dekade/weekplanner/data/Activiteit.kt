package be.dekade.weekplanner.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activiteiten")
data class Activiteit(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "activiteitId")
    var activiteitId: Long = 0L,
    var titel: String,
    var notities: String,
    var startuur: Int,
    var startminuut: Int,
    var isNotificatieAan: Boolean
)
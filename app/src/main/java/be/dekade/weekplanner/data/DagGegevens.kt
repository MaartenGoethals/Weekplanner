package be.dekade.weekplanner.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "dagGegevens", foreignKeys = [
        ForeignKey(
            onDelete = CASCADE,
            entity = Activiteit::class,
            parentColumns = ["activiteitId"],
            childColumns = ["activiteitReferenceId"]
        )
    ]
)
data class DagGegevens(
    @PrimaryKey(autoGenerate = true)
    var gegevensId: Long = 0L,
    var activiteitReferenceId: Long,
    var dag: Int,
    var isActief: Boolean,
    var uitstelUur: Int = -1,
    var uitstelMinuut: Int = -1,
    var isAfgewerkt: Boolean = false
)

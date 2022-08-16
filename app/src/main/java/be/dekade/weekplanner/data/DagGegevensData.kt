package be.dekade.weekplanner.data

import org.bson.types.ObjectId

data class DagGegevensData(
    var gegevensId: String = ObjectId().toHexString(),
    val activiteit: ActiviteitData? = null,
    var dag: Int,
    var isActief: Boolean,
    var uitstelUur: Int = -1,
    var uitstelMinuut: Int = -1,
    var isAfgewerkt: Boolean = false
)

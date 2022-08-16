package be.dekade.weekplanner.data

import org.bson.types.ObjectId

data class ActiviteitData(
    var activiteitId: String = ObjectId().toHexString(),
    var dagGegevens: List<DagGegevensData> = emptyList(),
    var titel: String,
    var notities: String,
    var startuur: Int,
    var startminuut: Int,
    var isNotificatieAan: Boolean
)

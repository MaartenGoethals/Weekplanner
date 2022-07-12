package be.dekade.weekplanner.data

import androidx.room.Embedded
import androidx.room.Relation
/*
 * Deze dataklasse geeft alle activiteitgegevens voor een hele week weer.
 */
data class ActiviteitEnDagGegevensWeek(
    @Embedded val activiteit: Activiteit,
    @Relation(
        parentColumn = "activiteitId",
        entityColumn = "activiteitReferenceId"
    )
    val dagGegevens: List<DagGegevens>
)

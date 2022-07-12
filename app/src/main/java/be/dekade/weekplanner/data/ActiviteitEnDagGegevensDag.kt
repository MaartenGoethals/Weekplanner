package be.dekade.weekplanner.data

import androidx.room.Embedded
import androidx.room.Relation

/*
 * Deze dataklasse geeft alle activiteitgegevens voor 1 enkele dag weer.
 */
data class ActiviteitEnDagGegevensDag(
    @Embedded val activiteit: Activiteit,

    @Embedded
    val dagGegevens: DagGegevens
)

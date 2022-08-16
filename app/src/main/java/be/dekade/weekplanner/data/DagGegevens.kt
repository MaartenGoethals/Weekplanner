package be.dekade.weekplanner.data

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class DagGegevens(
    @PrimaryKey
    var gegevensId: String = ObjectId().toHexString(),
    @LinkingObjects("dagGegevens")
    val activiteit: RealmResults<Activiteit>? = null,
    var dag: Int = 0,
    var isActief: Boolean = true,
    var uitstelUur: Int = -1,
    var uitstelMinuut: Int = -1,
    var isAfgewerkt: Boolean = false
) : RealmObject()

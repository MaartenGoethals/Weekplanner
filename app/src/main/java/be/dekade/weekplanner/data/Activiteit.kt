package be.dekade.weekplanner.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import org.bson.types.ObjectId

open class Activiteit(
    @PrimaryKey @RealmField("activiteitId")
    var activiteitId: String = ObjectId().toHexString(),
    var dagGegevens: RealmList<DagGegevens> = RealmList(),
    var titel: String = "",
    var notities: String = "",
    var startuur: Int = 8,
    var startminuut: Int = 0,
    var isNotificatieAan: Boolean = true
) : RealmObject(){
}
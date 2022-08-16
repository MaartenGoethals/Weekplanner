package be.dekade.weekplanner.data

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class ActiviteitenDagGegevensDao @Inject constructor(private val configuration: RealmConfiguration) {
    fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResults<T>(this)

    suspend fun insertActiviteit(activiteitData: ActiviteitData) {
        val realmDTB = Realm.getInstance(configuration)
        realmDTB.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val activiteit = Activiteit(
                activiteitId = activiteitData.activiteitId,
                startuur = activiteitData.startuur,
                startminuut = activiteitData.startminuut,
                titel = activiteitData.titel,
                notities = activiteitData.notities,
                isNotificatieAan = activiteitData.isNotificatieAan
            )
            for (gegeven: DagGegevensData in activiteitData.dagGegevens) {
                val gegevenRealm = mapDagGegevens(gegeven)
                activiteit.dagGegevens.add(gegevenRealm)
            }
            realmTransaction.insertOrUpdate(activiteit)
        }
    }

    /*suspend fun insertDagGegevensWeek(dagGegevensWeek: List<DagGegevens>) {
        realmDTB.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmTransaction.insertOrUpdate(dagGegevensWeek)
        }
    }*/

    suspend fun updateActiviteit(activiteit: ActiviteitData) {
        val realmDTB = Realm.getInstance(configuration)
        realmDTB.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val realmActiviteit = realmTransaction.where(Activiteit::class.java)
                .equalTo("activiteitId", activiteit.activiteitId).findFirst()
            realmActiviteit?.apply {
                titel = activiteit.titel
                notities = activiteit.notities
                isNotificatieAan = activiteit.isNotificatieAan
                startminuut = activiteit.startminuut
                startuur = activiteit.startuur
            }
        }
    }

    suspend fun updateDagGegevens(dagGegevens: List<DagGegevensData>) {
        val realmDTB = Realm.getInstance(configuration)
        realmDTB.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            for (item in dagGegevens) {
                val realmDagGegevens = realmTransaction.where(DagGegevens::class.java)
                    .equalTo("gegevensId", item.gegevensId).findFirst()
                realmDagGegevens?.apply {
                    uitstelMinuut = item.uitstelMinuut
                    uitstelUur = item.uitstelUur
                    isActief = item.isActief
                    isAfgewerkt = item.isAfgewerkt
                }
            }
        }
    }

    suspend fun updateDagGegevens(item: DagGegevensData) {
        val realmDTB = Realm.getInstance(configuration)
        realmDTB.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val realmDagGegevens = realmTransaction.where(DagGegevens::class.java)
                .equalTo("gegevensId", item.gegevensId).findFirst()
            realmDagGegevens?.apply {
                uitstelMinuut = item.uitstelMinuut
                uitstelUur = item.uitstelUur
                isActief = item.isActief
                isAfgewerkt = item.isAfgewerkt
            }
        }
    }

    fun getActiviteitEnDagGegevensWeek(id: String): ActiviteitData? {
        val realmDTB = Realm.getInstance(configuration)
        val activiteit = mapActiviteitGeenDaggegevens(realmDTB.where(Activiteit::class.java).equalTo("activiteitId", id)
            .findFirst())
        val dagGegevens = realmDTB.where(DagGegevens::class.java).equalTo("activiteit.activiteitId",id).findAll().map {
            mapDagGegevens(it)
        }
        activiteit?.dagGegevens = dagGegevens
        return activiteit
    }

    fun getActiviteitenEnDagGegevens(dagVanDeWeek: Int): List<DagGegevensData> {
        val realmDTB = Realm.getInstance(configuration)
        return realmDTB.where(DagGegevens::class.java).equalTo("dag", dagVanDeWeek)
            .equalTo("isActief", true).findAll().map{
                mapDagGegevens(it)
            }
    }

    fun getAlarms(): List<DagGegevensData> {
        val realmDTB = Realm.getInstance(configuration)
        return realmDTB.where(DagGegevens::class.java).equalTo("activiteit.isNotificatieAan", true)
            .equalTo("isActief", true).findAll().map {
                mapDagGegevens(it)
            }
    }

    suspend fun deleteActiviteit(activiteit: ActiviteitData) {
        val realmDTB = Realm.getInstance(configuration)
        realmDTB.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val activiteitTeVerwijderen = realmTransaction.where(Activiteit::class.java)
                .equalTo("activiteitId", activiteit.activiteitId).findFirst()
            activiteitTeVerwijderen?.dagGegevens?.deleteAllFromRealm()
            activiteitTeVerwijderen?.deleteFromRealm()
        }
    }

    private fun mapDagGegevens(dagGegevens: DagGegevens): DagGegevensData {
        return DagGegevensData(
            gegevensId = dagGegevens.gegevensId,
            activiteit = mapActiviteitGeenDaggegevens(dagGegevens.activiteit?.firstOrNull()),
            dag = dagGegevens.dag,
            isActief = dagGegevens.isActief,
            isAfgewerkt = dagGegevens.isAfgewerkt
        )
    }

    private fun mapDagGegevens(dagGegevens: DagGegevensData): DagGegevens {
        return DagGegevens(
            gegevensId = dagGegevens.gegevensId,
            dag = dagGegevens.dag,
            isActief = dagGegevens.isActief,
            isAfgewerkt = dagGegevens.isAfgewerkt
        )
    }

    private fun mapActiviteitGeenDaggegevens(activiteit: Activiteit?): ActiviteitData? {
        if (activiteit != null)
            return ActiviteitData(
                activiteitId = activiteit.activiteitId,
                dagGegevens = emptyList(),
                startuur = activiteit.startuur,
                startminuut = activiteit.startminuut,
                titel = activiteit.titel,
                notities = activiteit.notities,
                isNotificatieAan = activiteit.isNotificatieAan
            )
        else
            return null
    }
}
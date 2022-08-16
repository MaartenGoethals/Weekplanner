package be.dekade.weekplanner.data

import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiviteitEnDagGegevensRepository @Inject constructor(private val activiteitenDagGegevensDao: ActiviteitenDagGegevensDao) {
    fun getActiviteitenVoorDag(weekdag: Int): List<DagGegevensData> {
        val result = activiteitenDagGegevensDao.getActiviteitenEnDagGegevens(weekdag)
        return result
    }

    fun getActiviteitEnDaggegevens(id: String) = activiteitenDagGegevensDao.getActiviteitEnDagGegevensWeek(id)

    suspend fun postActiviteit(activiteit: ActiviteitData) = activiteitenDagGegevensDao.insertActiviteit(activiteit)

    //suspend fun postDagGegevens(dagGegevens: List<DagGegevensData>) = activiteitenDagGegevensDao.insertDagGegevensWeek(dagGegevens)

    suspend fun updateActiviteit(activiteit: ActiviteitData) = activiteitenDagGegevensDao.updateActiviteit(activiteit)

    suspend fun updateDagGegevens(gegevens : List<DagGegevensData>) = activiteitenDagGegevensDao.updateDagGegevens(
        gegevens)

    suspend fun updateDagGegevens(gegevens: DagGegevensData) = activiteitenDagGegevensDao.updateDagGegevens(gegevens)

    suspend fun deleteActiviteit(activiteit: ActiviteitData) = activiteitenDagGegevensDao.deleteActiviteit(activiteit)

    fun getAlarms() = activiteitenDagGegevensDao.getAlarms()
}
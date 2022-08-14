package be.dekade.weekplanner.data

import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiviteitEnDagGegevensRepository @Inject constructor(private val activiteitenDagGegevensDao: ActiviteitenDagGegevensDao) {
    fun getActiviteitenVoorDag(weekdag: Int): LiveData<List<ActiviteitEnDagGegevensDag>> {
        val result = activiteitenDagGegevensDao.getActiviteitenEnDagGegevens(weekdag)
        return result
    }

    fun getActiviteitEnDaggegevens(id: Long) = activiteitenDagGegevensDao.getActiviteitEnDagGegevensWeek(id)

    fun postActiviteit(activiteit: Activiteit) = activiteitenDagGegevensDao.insertActiviteit(activiteit)

    fun postDagGegevens(dagGegevens: List<DagGegevens>) = activiteitenDagGegevensDao.insertDagGegevensWeek(dagGegevens)

    fun updateActiviteit(activiteit: Activiteit) = activiteitenDagGegevensDao.updateActiviteit(activiteit)

    fun updateDagGegevens(gegevens : List<DagGegevens>) = activiteitenDagGegevensDao.updateDagGegevens(
        gegevens)

    fun updateDagGegevens(gegevens: DagGegevens) = activiteitenDagGegevensDao.updateDagGegevens(gegevens)

    fun deleteActiviteit(activiteit: Activiteit) = activiteitenDagGegevensDao.deleteActiviteit(activiteit)

    suspend fun getAlarms() = activiteitenDagGegevensDao.getAlarms()
}
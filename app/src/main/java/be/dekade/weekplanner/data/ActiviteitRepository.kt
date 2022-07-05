package be.dekade.weekplanner.data

import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiviteitRepository @Inject constructor(private val activiteitDao: ActiviteitDao) {
    fun getActiviteiten() = activiteitDao.getActiviteiten()

    fun getActiviteit(id: Int) = activiteitDao.getActiviteit(id)

    fun getActiviteitenVoorDag(weekdag: Int) = activiteitDao.getActiviteitenVoorWeekdag(weekdag)
}
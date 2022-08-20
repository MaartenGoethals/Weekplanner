package be.dekade.weekplanner.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface ActiviteitenDagGegevensDao {
    @Transaction
    @Query("SELECT * FROM activiteiten ORDER BY startuur, startminuut")
    fun getActiviteiten(): LiveData<List<ActiviteitEnDagGegevensWeek>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActiviteit(activiteit: Activiteit) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDagGegevensWeek(dagGegevensWeek: List<DagGegevens>)

    @Update
    fun updateActiviteit(activiteit: Activiteit)

    @Update
    fun updateDagGegevens(dagGegevens: List<DagGegevens>)

    @Update
    fun updateDagGegevens(dagGegevens: DagGegevens)

    @Transaction
    @Query("SELECT * FROM activiteiten WHERE activiteitId = :id")
    fun getActiviteitEnDagGegevensWeek(id: Long): LiveData<ActiviteitEnDagGegevensWeek>

    @Transaction
    @Query("SELECT * FROM activiteiten JOIN dagGegevens ON activiteitId = activiteitReferenceId WHERE dag = :dagVanDeWeek AND isActief = 1" )
    fun getActiviteitenEnDagGegevens(dagVanDeWeek: Int) : LiveData<List<ActiviteitEnDagGegevensDag>>

    @Transaction
    @Query("SELECT * FROM activiteiten JOIN dagGegevens ON activiteitId = activiteitReferenceId WHERE isNotificatieAan = 1 AND isActief = 1" )
    suspend fun getAlarms() : List<ActiviteitEnDagGegevensDag>

    @Delete
    fun deleteActiviteit(activiteit: Activiteit)
}
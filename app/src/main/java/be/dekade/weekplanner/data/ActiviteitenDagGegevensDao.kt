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
    fun updateActiviteit(vararg activiteit: Activiteit)

    @Update
    fun updateDagGegevens(vararg dagGegevens: DagGegevens)

    @Transaction
    @Query("SELECT * FROM activiteiten WHERE activiteitId = :id")
    fun getActiviteitEnDagGagevensWeek(id: Long): LiveData<ActiviteitEnDagGegevensWeek>

    @Transaction
    @Query("SELECT * FROM activiteiten JOIN dagGegevens ON activiteitId = activiteitReferenceId WHERE dag = :dagVanDeWeek AND isActief = 1" )
    fun getActiviteitenEnDagGegevens(dagVanDeWeek: Int) : LiveData<List<ActiviteitEnDagGegevensDag>>

    @Delete
    fun deleteActiviteit(activiteit: Activiteit)
}
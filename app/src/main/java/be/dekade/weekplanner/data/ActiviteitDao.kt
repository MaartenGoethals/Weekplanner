package be.dekade.weekplanner.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface ActiviteitDao {
    @Query("SELECT * FROM activiteiten ORDER BY startuur, startminuut")
    fun getActiviteiten(): LiveData<List<Activiteit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activiteit: Activiteit)

    @Query("SELECT * FROM activiteiten WHERE id = :id")
    fun getActiviteit(id: Int): Activiteit

    fun getActiviteitenVoorWeekdag(weekdag: Int): LiveData<List<Activiteit>> {
        when(weekdag) {
            0 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isMaandag = 1 ORDER BY startuur, startminuut"))
            1 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isDinsdag = 1 ORDER BY startuur, startminuut"))
            2 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isWoensdag = 1 ORDER BY startuur, startminuut"))
            3 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isDonderdag = 1 ORDER BY startuur, startminuut"))
            4 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isVrijdag = 1 ORDER BY startuur, startminuut"))
            5 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isZaterdag = 1 ORDER BY startuur, startminuut"))
            6 -> return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten WHERE isZondag = 1 ORDER BY startuur, startminuut"))
            else ->{
                return getActiviteiten(SimpleSQLiteQuery("SELECT * FROM activiteiten ORDER BY startuur, startminuut"))
            }
        }
    }

    @RawQuery(observedEntities = arrayOf(Activiteit::class))
    fun getActiviteiten(query: SupportSQLiteQuery): LiveData<List<Activiteit>>
}
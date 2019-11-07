package org.seniorlaguna.calculator

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * GlobalDatabase is a room database
 * applying the singleton pattern
 */

@Database(entities = arrayOf(Calculation::class), version = 2, exportSchema = false)
abstract class GlobalDatabase : RoomDatabase() {

    // apply singleton pattern
    companion object {

        private val DB_FILE = "global.db"
        private var instance : GlobalDatabase? = null

        fun getInstance(context: Context) : GlobalDatabase {

            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, GlobalDatabase::class.java, DB_FILE)
                    .allowMainThreadQueries()
                    .build()
            }

            assert(instance != null)
            return instance!!
        }

    }

    abstract fun dao() : GlobalDao

}

@Dao
interface GlobalDao {

    @Insert
    fun insertCalculation(calculation: Calculation)

    @Update
    fun updateCalculation(calculation: Calculation)

    @Delete
    fun deleteCalculation(calculation: Calculation)

    @Query("DELETE FROM Calculation WHERE type = :calculationType")
    fun clearCalculationHistory(calculationType : Int)

    @Query("SELECT * FROM Calculation WHERE type = :calculationType ORDER BY id DESC")
    fun getAllCalculations(calculationType: Int) : LiveData<List<Calculation>>

    @Query("DELETE FROM Calculation WHERE type = :calculationType AND id NOT IN (SELECT id FROM Calculation WHERE type = :calculationType ORDER BY id DESC LIMIT :limit)")
    fun adjustSize(calculationType: Int, limit : Int)


}

@Entity
class Calculation(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo var title : String,
    @ColumnInfo var calculation : String,
    @ColumnInfo val type : Int
) {

    companion object {
        const val TYPE_BASIC = 1
        const val TYPE_SCIENTIFIC = 2
    }

}



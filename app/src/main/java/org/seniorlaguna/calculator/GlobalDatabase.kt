package org.seniorlaguna.calculator

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * GlobalDatabase is a room database
 * applying the singleton pattern
 */

@Database(
    entities = arrayOf(Calculation::class),
    version = 4,
    exportSchema = false
)
abstract class GlobalDatabase : RoomDatabase() {

    // apply singleton pattern
    companion object {

        private val DB_FILE = "global.db"
        private var instance : GlobalDatabase? = null

        private val migration2To3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `Function` (`title` TEXT NOT NULL, `expression` TEXT NOT NULL, " +
                        "PRIMARY KEY(`title`))")

                database.execSQL("CREATE TABLE `Constant` (`title` TEXT NOT NULL, `value` TEXT NOT NULL, " +
                        "PRIMARY KEY(`title`))")
            }
        }

        private val migration3To4 = object : Migration(3,4) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }

        fun getInstance(context: Context) : GlobalDatabase {

            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, GlobalDatabase::class.java, DB_FILE)
                    .addMigrations(migration2To3, migration3To4)
                    .allowMainThreadQueries()
                    .build()
            }

            if (instance == null) throw Exception("Global database can't be instantiated")
            return instance!!
        }

    }

    abstract fun dao() : GlobalDao

}

@Dao
interface GlobalDao {

    // Calculations
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
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
    entities = arrayOf(Calculation::class, Constant::class, Function::class),
    version = 3,
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

        fun getInstance(context: Context) : GlobalDatabase {

            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, GlobalDatabase::class.java, DB_FILE)
                    .addMigrations(migration2To3)
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

    // Constants
    @Insert
    fun insertConstant(constant: Constant)

    @Update
    fun updateConstant(constant: Constant)

    @Delete
    fun deleteConstant(constant: Constant)

    @Query("SELECT * FROM Constant")
    fun getAllConstantsLive() : LiveData<List<Constant>>

    @Query("SELECT * FROM Constant")
    fun getAllConstants() : List<Constant>

    // Functions
    @Insert
    fun insertFunction(function: Function)

    @Update
    fun updateFunction(function: Function)

    @Delete
    fun deleteFunction(function: Function)

    @Query("SELECT * FROM Function")
    fun getAllFunctionsLive() : LiveData<List<Function>>

    @Query("SELECT * FROM Function")
    fun getAllFunctions() : List<Function>

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

@Entity
class Function(
    @PrimaryKey var title : String,
    @ColumnInfo var expression : String
)

@Entity
class Constant(
    @PrimaryKey var title : String,
    @ColumnInfo var value : String
)


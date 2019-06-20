package org.seniorlaguna.calculator.basic.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Calculation::class), version = 1)
abstract class CalculationDatabase : RoomDatabase() {

    companion object {
        private var instance : CalculationDatabase? = null

        fun getInstance(context: Context) : CalculationDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, CalculationDatabase::class.java, "basic.db").allowMainThreadQueries().build()
            }

            return instance!!
        }
    }

    abstract fun dao() : CalculationDao
}
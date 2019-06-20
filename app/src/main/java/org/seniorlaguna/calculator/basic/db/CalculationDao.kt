package org.seniorlaguna.calculator.basic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CalculationDao {

    @Insert
    fun insert(calculation: Calculation)

    @Update
    fun update(calculation: Calculation)

    @Query("SELECT * FROM Calculation ORDER BY id DESC")
    fun getAll() : LiveData<List<Calculation>>

    @Query("DELETE FROM Calculation")
    fun deleteAll()

    @Query("DELETE FROM Calculation WHERE id NOT IN (SELECT id FROM Calculation ORDER BY id DESC LIMIT :limit)")
    fun adjustSize(limit : Int)

    @Delete
    fun delete(calculation: Calculation)
}
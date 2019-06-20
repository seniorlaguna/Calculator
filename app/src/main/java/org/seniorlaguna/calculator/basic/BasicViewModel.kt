package org.seniorlaguna.calculator.basic

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import org.seniorlaguna.calculator.basic.db.Calculation
import org.seniorlaguna.calculator.basic.db.CalculationDatabase
import org.seniorlaguna.calculator.basic.settings.BasicSettings

class BasicViewModel(app : Application) : AndroidViewModel(app) {

    val settings = BasicSettings.getInstance(app.applicationContext)
    private val dao = CalculationDatabase.getInstance(app.applicationContext).dao()

    fun addCalculation(calculation: Calculation) {
        if (!settings.history) return
        dao.insert(calculation)
        dao.adjustSize(settings.historyLength)
    }

    fun updateCalculation(calculation: Calculation) = dao.update(calculation)

    fun deleteAll() = dao.deleteAll()

    fun delete(calculation: Calculation) = dao.delete(calculation)

    fun getAll() = dao.getAll()

    fun adjustSize(limit : Int) {
        dao.adjustSize(limit)
    }

}
package org.seniorlaguna.calculator.tool.scientific

import android.app.Application
import org.seniorlaguna.calculator.GlobalDatabase
import org.seniorlaguna.calculator.apc.Calculator
import org.seniorlaguna.calculator.settings.GlobalSettings
import org.seniorlaguna.calculator.tool.basic.BasicViewModel

class ScientificViewModel(application: Application) : BasicViewModel(application) {

    override val settings = GlobalSettings.getInstance(application)
    val database = GlobalDatabase.getInstance(application.applicationContext).dao()
    override val calculator = Calculator(GlobalSettings.getInstance(application))


}
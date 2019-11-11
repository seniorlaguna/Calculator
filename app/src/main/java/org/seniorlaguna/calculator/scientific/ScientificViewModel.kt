package org.seniorlaguna.calculator.scientific

import android.app.Application
import org.seniorlaguna.calculator.GlobalSettings
import org.seniorlaguna.calculator.basic.BasicViewModel
import org.seniorlaguna.calculator.basic.Calculator

class ScientificViewModel(application: Application) : BasicViewModel(application) {

    override val settings = GlobalSettings.getInstance(application).scientificCalculator
    override val calculator = Calculator(GlobalSettings.getInstance(application).scientificCalculator)


}
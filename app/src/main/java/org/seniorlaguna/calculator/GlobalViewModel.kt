package org.seniorlaguna.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.seniorlaguna.calculator.settings.GlobalSettings

class GlobalViewModel(application: Application) : AndroidViewModel(application) {

    val settings = GlobalSettings.getInstance(application.applicationContext)
    val database = AsyncDatabase(GlobalDatabase.getInstance(application.applicationContext).dao())
    val toolbarTitle : MutableLiveData<String> = MutableLiveData()
}
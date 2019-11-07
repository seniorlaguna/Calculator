package org.seniorlaguna.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class GlobalViewModel(application: Application) : AndroidViewModel(application) {

    val settings = GlobalSettings.getInstance(application.applicationContext)
    val database = GlobalDatabase.getInstance(application.applicationContext).dao()
    val toolbarTitle : MutableLiveData<String> = MutableLiveData()
}
package com.sign.deftpdf

import android.app.Application
import com.google.firebase.FirebaseApp
import com.sign.deftpdf.model.user.UserData
import com.sign.deftpdf.util.Util

class DeftApp : Application() {

    companion object {
        lateinit var user : UserData
        var sortTypeHome = Util.SORT_DESC
        var filterTypeHome = Util.FILTER_MONTH
        var sortTypeDocuments= Util.SORT_DESC
        var filterTypeDocuments = Util.FILTER_MONTH
        var sortTypeLibrary = Util.SORT_DESC
        var filterTypeLibrary = Util.FILTER_MONTH
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
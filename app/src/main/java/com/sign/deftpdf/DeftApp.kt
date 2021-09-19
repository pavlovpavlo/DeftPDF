package com.sign.deftpdf

import android.app.Application
import com.sign.deftpdf.model.user.UserData
import com.sign.deftpdf.util.Util

class DeftApp : Application() {

    companion object {
        lateinit var user : UserData
        var sortTypeHome = "desc"
        var filterTypeHome = Util.FILTER_MONTH
    }
}
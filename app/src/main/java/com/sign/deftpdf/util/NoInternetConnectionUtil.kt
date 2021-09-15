package com.sign.deftpdf.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.sign.deftpdf.custom_views.NoInternetConnectionDialog.Companion.display

class NoInternetConnectionUtil {
    val NO_INTERNET_CODE = 101
    fun isInternetOn(context: Context): Boolean {
        return isMobileOrWifiConnectivityAvailable(context)
    }

    private fun isMobileOrWifiConnectivityAvailable(ctx: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        try {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName.equals("WIFI", ignoreCase = true)) if (ni.isConnected) {
                    haveConnectedWifi = true
                }
                if (ni.typeName.equals("MOBILE", ignoreCase = true)) if (ni.isConnected) {
                    haveConnectedMobile = true
                }
            }
        } catch (e: Exception) {
            println("[ConnectionVerifier] inside isInternetOn() Exception is : $e")
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    fun showNoInternetDialog(activity: AppCompatActivity, onRefreshResponse: () -> Unit) {
        display(activity.supportFragmentManager, onRefreshResponse)
    }
}
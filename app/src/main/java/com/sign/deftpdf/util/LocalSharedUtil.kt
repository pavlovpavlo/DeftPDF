package com.sign.deftpdf.util

import android.content.Context
import android.preference.PreferenceManager

class LocalSharedUtil {

    private val LOCAL_STORAGE_TOKEN_PARAMETER = "deftpdf_token"

    fun setTokenParameter(value: String, context: Context?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(LOCAL_STORAGE_TOKEN_PARAMETER, value)
        editor.apply()
    }

    fun getTokenParameter(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(LOCAL_STORAGE_TOKEN_PARAMETER, "")!!
    }
}
package com.sign.deftpdf.model.login

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


class AuthData {
    @SerializedName("token")
    var token: String? = null
}
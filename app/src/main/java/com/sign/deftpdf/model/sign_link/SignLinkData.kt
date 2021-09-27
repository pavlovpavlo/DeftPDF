package com.sign.deftpdf.model.sign_link

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


class SignLinkData {
    @SerializedName("url")
    var url: String? = null
}
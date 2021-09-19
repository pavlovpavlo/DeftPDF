package com.sign.deftpdf.model

import com.google.gson.annotations.SerializedName


open class BaseModel() {
    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("message")
    var message: List<String>? = null

}
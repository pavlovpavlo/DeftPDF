package com.sign.deftpdf.model

import com.google.gson.annotations.SerializedName

class SignatureBody {
    @SerializedName("api_token")
    var token: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("string_signature")
    var signSignature: String? = null
}
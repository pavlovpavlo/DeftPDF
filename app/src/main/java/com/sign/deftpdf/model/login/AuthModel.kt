package com.sign.deftpdf.model.login

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel


class AuthModel : BaseModel() {
    @SerializedName("data")
    var data: AuthData? = null
}

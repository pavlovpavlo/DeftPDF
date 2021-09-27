package com.sign.deftpdf.model.sign_link

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel


class SignLinkModel : BaseModel() {
    @SerializedName("data")
    var data: SignLinkData? = null
}

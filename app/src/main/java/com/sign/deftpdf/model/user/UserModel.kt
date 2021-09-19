package com.sign.deftpdf.model.user

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel

class UserModel : BaseModel() {
    @SerializedName("data")
    var data: UserData? = null
}

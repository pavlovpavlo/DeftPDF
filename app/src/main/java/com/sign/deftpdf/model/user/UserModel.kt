package com.sign.deftpdf.model.user

import com.sign.deftpdf.model.BaseModel
import com.squareup.moshi.Json

class UserModel : BaseModel() {
    @field:Json(name = "data")
    var data: UserData? = null
}

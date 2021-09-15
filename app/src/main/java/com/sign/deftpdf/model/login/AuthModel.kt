package com.sign.deftpdf.model.login

import com.sign.deftpdf.model.BaseModel
import com.squareup.moshi.Json


class AuthModel : BaseModel() {
    @field:Json(name = "data")
    var data: AuthData? = null
}

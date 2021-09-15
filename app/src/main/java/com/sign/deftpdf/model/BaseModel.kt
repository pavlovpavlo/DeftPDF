package com.sign.deftpdf.model

import com.squareup.moshi.Json


open class BaseModel() {
    @field:Json(name = "success")
    var success: Boolean? = null

    @field:Json(name = "message")
    var message: List<String>? = null

}
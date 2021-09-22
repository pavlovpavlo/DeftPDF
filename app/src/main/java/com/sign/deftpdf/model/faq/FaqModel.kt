package com.sign.deftpdf.model.faq

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel


class FaqModel : BaseModel() {
    @SerializedName("data")
    var data: List<FaqData>? = null
}

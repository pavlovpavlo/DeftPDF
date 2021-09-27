package com.sign.deftpdf.model.document

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel


class DocumentModel : BaseModel() {
    @SerializedName("data")
    var data: DocumentData? = null
}

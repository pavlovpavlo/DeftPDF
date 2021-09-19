package com.sign.deftpdf.model.documents

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel

class DocumentsModel : BaseModel() {
    @SerializedName("data")
    var data: MutableList<DocumentData>? = null

    @SerializedName("info")
    var info: DocumentInfo? = null
}

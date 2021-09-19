package com.sign.deftpdf.model.documents

import com.google.gson.annotations.SerializedName

class DocumentInfo {
    @SerializedName("page")
    var page = 0

    @SerializedName("last_page")
    var lastPage = 0

    @SerializedName("perPage")
    var perPage = 0

    @SerializedName("sortBy")
    var sortBy: String? = null

    @SerializedName("sortType")
    var sortType: String? = null

    @SerializedName("count")
    var count = 0
}
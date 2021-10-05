package com.sign.deftpdf.model.user

import com.google.gson.annotations.SerializedName


class UserSubscription {
    @SerializedName("status")
    var status: Boolean = false

    @SerializedName("free_document")
    var freeDocument: Int = 0

    @SerializedName("type")
    var type: String = "basic"

    @SerializedName("limit_paid_documents")
    var limitPaidDocuments: Int = 0
}
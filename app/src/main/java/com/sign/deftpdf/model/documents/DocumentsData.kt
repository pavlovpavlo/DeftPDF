package com.sign.deftpdf.model.documents

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DocumentsData : Serializable{
    @SerializedName("id")
    var id = 0

    @SerializedName("user_id")
    var userId = 0

    @SerializedName("status")
    var status: String? = null

    @SerializedName("operation_type")
    var operationType: String? = null

    @SerializedName("original_name")
    var originalName: String? = null

    @SerializedName("original_document")
    var originalDocument: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("deleted_at")
    var deletedAt: String? = null

    @SerializedName("delete_after")
    var deleteAfter = 0
}
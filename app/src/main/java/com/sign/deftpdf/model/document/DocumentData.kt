package com.sign.deftpdf.model.document

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class DocumentData {

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("user_id")
    @Expose
    var userId = 0

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("size")
    @Expose
    var size = 0

    @SerializedName("original_name")
    @Expose
    var originalName: String? = null

    @SerializedName("original_document")
    @Expose
    var originalDocument: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("activity")
    @Expose
    var activity: List<DocumentActivities>? = null
}
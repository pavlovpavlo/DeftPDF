package com.sign.deftpdf.model.document

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DocumentActivities {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("user_id")
    @Expose
    var userId = 0

    @SerializedName("parent_id")
    @Expose
    var parentId = 0

    @SerializedName("share_email")
    @Expose
    var shareEmail: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}
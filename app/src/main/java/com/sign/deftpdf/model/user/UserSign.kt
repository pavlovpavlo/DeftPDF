package com.sign.deftpdf.model.user

import com.google.gson.annotations.SerializedName

class UserSign {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null
}
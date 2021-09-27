package com.sign.deftpdf.model.notifications

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class NotificationData {
    @SerializedName("id")
    var id = 0

    @SerializedName("notification")
    var notification: String? = null

    @SerializedName("user_id")
    @Expose
    var userId = 0

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}
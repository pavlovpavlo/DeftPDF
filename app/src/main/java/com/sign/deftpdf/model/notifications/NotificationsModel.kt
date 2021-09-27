package com.sign.deftpdf.model.notifications

import com.google.gson.annotations.SerializedName
import com.sign.deftpdf.model.BaseModel

class NotificationsModel : BaseModel() {
    @SerializedName("data")
    var data: MutableList<NotificationData>? = null
}

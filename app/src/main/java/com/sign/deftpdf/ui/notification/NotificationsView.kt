package com.sign.deftpdf.ui.notification

import com.sign.deftpdf.model.notifications.NotificationsModel
import com.sign.deftpdf.model.user.UserModel

interface NotificationsView {
    fun getNotificationsSuccess(data: NotificationsModel)
}
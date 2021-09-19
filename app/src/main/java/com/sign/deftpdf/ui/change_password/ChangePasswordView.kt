package com.sign.deftpdf.ui.change_password

import com.sign.deftpdf.model.user.UserModel

interface ChangePasswordView {
    fun passwordChangeSuccess(data: UserModel)
}
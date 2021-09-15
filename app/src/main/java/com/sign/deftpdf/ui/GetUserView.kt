package com.sign.deftpdf.ui

import com.sign.deftpdf.model.user.UserModel

interface GetUserView {
    fun getUserSuccessSuccess(data: UserModel)
}
package com.sign.deftpdf.ui.main

import com.sign.deftpdf.model.user.UserModel

interface GetUserView {
    fun getUserSuccess(data: UserModel)
}
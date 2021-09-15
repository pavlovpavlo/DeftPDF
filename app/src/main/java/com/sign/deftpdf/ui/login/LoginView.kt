package com.sign.deftpdf.ui.login

import com.sign.deftpdf.model.login.AuthModel

interface LoginView {
    fun loginSuccess(data : AuthModel)
}
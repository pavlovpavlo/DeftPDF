package com.sign.deftpdf.ui.registration

import com.sign.deftpdf.model.login.AuthModel

interface CreateAccountView {
    fun createAccountSuccess(data : AuthModel)
}
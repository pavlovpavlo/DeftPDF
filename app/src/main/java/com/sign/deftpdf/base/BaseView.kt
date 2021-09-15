package com.sign.deftpdf.base

interface BasicView {
    fun showError(message: String)
    fun showInternetError(onRefreshResponse: () -> Unit)
    fun startLoader()
    fun stopLoader()
}
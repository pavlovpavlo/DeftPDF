package com.sign.deftpdf.base

public open class BasePresenter(private var basicView: BasicView) : BasicView {
    override fun showError(message: String) {
        basicView.showError(message.replace("[","").replace("]",""))
    }

    override fun showInternetError(onRefreshResponse: () -> Unit) {
        basicView.showInternetError(onRefreshResponse)
    }

    override fun startLoader() {
        basicView.startLoader()
    }

    override fun stopLoader() {
        basicView.stopLoader()
    }
}
package com.sign.deftpdf.ui.notification

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NotificationsPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: NotificationsView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: NotificationsView) {
        this.view = view
    }

    fun sendResponse(token: String) {
        super.startLoader()
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            apiService.getNotifications(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.success == true)
                        view.getNotificationsSuccess(response)
                    else
                        super.showError(response.message.toString())
                    super.stopLoader()
                },
                    { t ->
                        if (t is SocketTimeoutException || t is UnknownHostException) super.showInternetError {
                            sendResponse(
                                token
                            )
                        } else {
                            super.showError("Error connection")
                        }
                        super.stopLoader()
                    })
        )


    }
}
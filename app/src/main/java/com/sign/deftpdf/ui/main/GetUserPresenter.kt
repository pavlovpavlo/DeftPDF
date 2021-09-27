package com.sign.deftpdf.ui.main

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GetUserPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: GetUserView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: GetUserView) {
        this.view = view
    }

    fun sendResponse(token: String) {
        super.startLoader()
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            apiService.getCurrentUser(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.success == true)
                        view.getUserSuccess(response)
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
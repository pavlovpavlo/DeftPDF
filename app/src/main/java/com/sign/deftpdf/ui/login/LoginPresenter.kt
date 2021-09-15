package com.sign.deftpdf.ui.login

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class LoginPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: LoginView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: LoginView) {
        this.view = view
    }

    fun sendResponse(email: String, password: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            apiService.login(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.success == true)
                        view.loginSuccess(response)
                    else
                        super.showError(response.message.toString())
                    super.stopLoader()
                },
                    { t ->
                        if (t is SocketTimeoutException || t is UnknownHostException) super.showInternetError {
                            sendResponse(
                                email,
                                password
                            )
                        } else {
                            super.showError("Error connection")
                        }
                        super.stopLoader()
                    })
        )


    }
}
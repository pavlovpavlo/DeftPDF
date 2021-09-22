package com.sign.deftpdf.ui.registration

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CreateAccountPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: CreateAccountView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: CreateAccountView) {
        this.view = view
    }

    fun sendResponse(email: String, password: String, name: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            apiService.registration(email, password, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.success == true)
                        view.createAccountSuccess(response)
                    else
                        super.showError(response.message.toString())
                    super.stopLoader()
                },
                    { t ->
                        if (t is SocketTimeoutException || t is UnknownHostException) super.showInternetError {
                            sendResponse(
                                email,
                                password,
                                name
                            )
                        } else {
                            super.showError(t.message.toString())
                        }
                        super.stopLoader()
                    })
        )


    }
}
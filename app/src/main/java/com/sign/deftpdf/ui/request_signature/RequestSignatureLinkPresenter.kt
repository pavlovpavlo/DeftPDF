package com.sign.deftpdf.ui.request_signature

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RequestSignatureLinkPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: RequestLinkView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: RequestLinkView) {
        this.view = view
    }

    fun sendResponse(token: String, documentId: String, email: String, name: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
                apiService.createSignLink(documentId,token, email, name)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ response ->
                            if (response.success == true)
                                view.requestSuccess(response)
                            else
                                super.showError(response.message.toString())
                            super.stopLoader()
                        },
                        { t ->
                            if (t is SocketTimeoutException || t is UnknownHostException) super.showInternetError {
                                sendResponse(
                                        token, documentId, email, name
                                )
                            } else {
                                super.showError("Error connection")
                            }
                            super.stopLoader()
                        })
        )


    }
}
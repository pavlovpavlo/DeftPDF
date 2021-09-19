package com.sign.deftpdf.ui.main

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class StoreDocumentPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: StoreDocumentView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: StoreDocumentView) {
        this.view = view
    }

    fun sendResponse(token: String, file: MultipartBody.Part) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(
                apiService.storeDocument(token, file)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ response ->
                            if (response.success == true)
                                view.storeDocumentSuccess(response)
                            else
                                super.showError(response.message.toString())
                            super.stopLoader()
                        },
                                { t ->
                                    if (t is SocketTimeoutException || t is UnknownHostException) super.showInternetError {
                                        sendResponse(
                                                token, file
                                        )
                                    } else {
                                        super.showError("Error connection")
                                    }
                                    super.stopLoader()
                                })
        )


    }
}
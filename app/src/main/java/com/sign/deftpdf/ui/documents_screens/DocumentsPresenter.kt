package com.sign.deftpdf.ui.documents_screens

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DocumentsPresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: DocumentsView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: DocumentsView) {
        this.view = view
    }

    fun sendResponse(token: String, page: String,
                     perPage: String, sortBy: String,
                     sortType: String, fromDate: String,
                     toDate: String, status: String?) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
                apiService.getDocuments(token, page, perPage, sortBy, sortType, fromDate, toDate, status)
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
                                        token, page, perPage, sortBy, sortType, fromDate, toDate, status
                                )
                            } else {
                                super.showError("Error connection")
                            }
                            super.stopLoader()
                        })
        )


    }
}
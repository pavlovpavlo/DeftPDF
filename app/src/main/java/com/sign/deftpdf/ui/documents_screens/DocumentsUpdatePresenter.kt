package com.sign.deftpdf.ui.documents_screens

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DocumentsUpdatePresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: BaseModelView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: BaseModelView) {
        this.view = view
    }

    fun sendResponse(
        token: String, documentId: String,
        file: MultipartBody.Part?, status: String?,
        name: String?
    ) {
        super.startLoader()

        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            apiService.updateDocument(documentId, token, file, status, name)
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
                                token, documentId, file, status, name
                            )
                        } else {
                            super.showError("Error connection")
                        }
                        super.stopLoader()
                    })
        )


    }
}
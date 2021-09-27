package com.sign.deftpdf.ui.draw

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import com.sign.deftpdf.ui.main.GetUserView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SignatureUpdatePresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: GetUserView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: GetUserView) {
        this.view = view
    }

    fun sendResponse(token: String, documentId: String,
                     image: MultipartBody.Part?, type: String,
                     signSignature: String?) {
        super.startLoader()
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
                apiService.updateSignature(documentId, token, image, type, signSignature)
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
                                        token, documentId, image, type, signSignature
                                )
                            } else {
                                super.showError("Error connection")
                            }
                            super.stopLoader()
                        })
        )


    }

    fun sendResponseStore(token: String,
                     image: MultipartBody.Part?, type: String,
                     signSignature: String?) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            apiService.storeSignature(token, image, type, signSignature)
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
                            sendResponseStore(
                                token, image, type, signSignature
                            )
                        } else {
                            super.showError("Error connection")
                        }
                        super.stopLoader()
                    })
        )


    }
}
package com.sign.deftpdf.ui.draw

import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import com.sign.deftpdf.base.BasePresenter
import com.sign.deftpdf.base.BasicView
import com.sign.deftpdf.model.SignatureBody
import com.sign.deftpdf.ui.main.GetUserView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class SignatureUpdatePresenter(basicView: BasicView) : BasePresenter(basicView) {
    private lateinit var view: GetUserView
    private var apiService: ApiService = RetrofitClient.ServiceBuilder.buildService()

    fun attachView(view: GetUserView) {
        this.view = view
    }

    fun sendResponse(
        token: String, documentId: String,
        image: MultipartBody.Part?, type: String,
        signSignature: String?
    ) {
        super.startLoader()
        val compositeDisposable = CompositeDisposable()
        val map: MutableMap<String, RequestBody> = HashMap()
        map["api_token"] = toRequestBody(token)
        map["type"] = toRequestBody(type)
        if (signSignature != null)
            map["string_signature"] = toRequestBody(signSignature)

        compositeDisposable.add(
            apiService.updateSignature(documentId, image, map)
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

    fun sendResponseStore(
        token: String,
        image: MultipartBody.Part?, type: String,
        signSignature: String?
    ) {
        val compositeDisposable = CompositeDisposable()
        val map: MutableMap<String, RequestBody> = HashMap()
        map["api_token"] = toRequestBody(token)
        map["type"] = toRequestBody(type)
        if (signSignature != null)
            map["string_signature"] = toRequestBody(signSignature)

        compositeDisposable.add(
            apiService.storeSignature(image, map)
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

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }
}
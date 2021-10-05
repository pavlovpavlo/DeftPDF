package com.sign.deftpdf.api

import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.SignatureBody
import com.sign.deftpdf.model.document.DocumentModel
import com.sign.deftpdf.model.documents.DocumentsModel
import com.sign.deftpdf.model.faq.FaqModel
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.model.login.AuthModel
import com.sign.deftpdf.model.notifications.NotificationsModel
import com.sign.deftpdf.model.sign_link.SignLinkModel
import com.sign.deftpdf.ui.main.GetUserView
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Headers("Accept: application/json")
    @POST("login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Observable<AuthModel>

    @Headers("Accept: application/json")
    @POST("register")
    fun registration(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String
    ): Observable<AuthModel>

    @Headers("Accept: application/json")
    @GET("user")
    fun getCurrentUser(@Query("api_token") token: String): Observable<UserModel>

    @Headers("Accept: application/json")
    @GET("faq")
    fun getFAQ(): Observable<FaqModel>

    @Headers("Accept: application/json")
    @POST("password/send-reset-link")
    fun forgotPassword(@Query("email") email: String): Observable<BaseModel>

    @Headers("Accept: application/json")
    @GET("user/documents")
    fun getDocuments(
        @Query("api_token") token: String,
        @Query("page") page: String,
        @Query("perPage") perPage: String,
        @Query("sortBy") sortBy: String,
        @Query("sortType") sortType: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("status") status: String?
    ): Observable<DocumentsModel>

    @Headers("Accept: application/json")
    @POST("user/profile/update")
    fun changePassword(
        @Query("api_token") token: String,
        @Query("password") password: String
    ): Observable<UserModel>

    @Multipart
    @Headers("Accept: application/json")
    @POST("user/documents/store")
    fun storeDocument(
        @Query("api_token") token: String,
        @Part file: MultipartBody.Part
    ): Observable<BaseModel>

    @Multipart
    @Headers("Accept: application/json")
    @POST("user/documents/update/{document_id}")
    fun updateDocument(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Query("api_token") token: String,
        @Part file: MultipartBody.Part?,
        @Query("status") status: String?,
        @Query("original_name") name: String?
    ): Observable<BaseModel>

    @Headers("Accept: application/json")
    @POST("user/documents/delete/{document_id}")
    fun deleteDocument(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Query("api_token") token: String
    ): Observable<BaseModel>

    @Headers("Accept: application/json")
    @POST("user/documents/send-sign-email/{document_id}")
    fun sendSignEmail(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Query("api_token") token: String,
        @Query("email") email: String,
        @Query("recipient_name") name: String,
        @Query("note") note: String
    ): Observable<BaseModel>

    @Headers("Accept: application/json")
    @POST("user/documents/create-sign-link/{document_id}")
    fun createSignLink(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Query("api_token") token: String,
        @Query("email") email: String,
        @Query("recipient_name") name: String
    ): Observable<SignLinkModel>

    @Headers("Accept: application/json")
    @GET("user/documents/show/{document_id}")
    fun getDocument(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Query("api_token") token: String
    ): Observable<DocumentModel>

    @Headers("Accept: application/json")
    @POST("user/sign-initial/delete/{document_id}")
    fun deleteSign(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Query("api_token") token: String
    ): Observable<BaseModel>

    @Multipart
    @Headers("Accept: application/json")
    @POST("user/sign-initial/update/{document_id}")
    fun updateSignature(
        @Path(value = "document_id", encoded = true) documentId: String,
        @Part image: MultipartBody.Part?,
        @PartMap params:MutableMap <String, @JvmSuppressWildcards RequestBody>
    ): Observable<UserModel>

    @Multipart
    @Headers("Accept: application/json")
    @POST("user/sign-initial/store")
    fun storeSignature(
        @Part image: MultipartBody.Part?,
        @PartMap params:MutableMap<String, @JvmSuppressWildcards RequestBody>
    ): Observable<UserModel>

    @Headers("Accept: application/json")
    @GET("user/notification")
    fun getNotifications(@Query("api_token") token: String): Observable<NotificationsModel>

    @Headers("Accept: application/json")
    @POST("subscription/cancel")
    fun cancelSubscription(
        @Query("api_token") token: String
    ): Observable<UserModel>

}
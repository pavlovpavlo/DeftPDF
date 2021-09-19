package com.sign.deftpdf.api

import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.documents.DocumentsModel
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.model.login.AuthModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @Headers("Accept: application/json")
    @POST("login")
    fun login(@Query("email") email: String, @Query("password") password: String): Observable<AuthModel>

    @Headers("Accept: application/json")
    @POST("register")
    fun registration(@Query("email") email: String,
                     @Query("password") password: String,
                     @Query("name") name: String): Observable<AuthModel>

    @Headers("Accept: application/json")
    @GET("user")
    fun getCurrentUser(@Query("api_token") token: String): Observable<UserModel>

    @Headers("Accept: application/json")
    @POST("password/send-reset-link")
    fun forgotPassword(@Query("email") email: String): Observable<BaseModel>

    @Headers("Accept: application/json")
    @GET("user/documents")
    fun getDocuments(@Query("api_token") token: String,
                     @Query("page") page: String,
                     @Query("perPage") perPage: String,
                     @Query("sortBy") sortBy: String,
                     @Query("sortType") sortType: String,
                     @Query("fromDate") fromDate: String,
                     @Query("toDate") toDate: String,
                     @Query("status") status: String?): Observable<DocumentsModel>

    @Headers("Accept: application/json")
    @POST("user/profile/update")
    fun changePassword(@Query("api_token") token: String, @Query("password") password: String): Observable<UserModel>

    @Multipart
    @Headers("Accept: application/json")
    @POST("user/documents/store")
    fun storeDocument(@Query("api_token") token: String, @Part file: MultipartBody.Part): Observable<BaseModel>

}
package com.sign.deftpdf.api

import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.model.login.AuthModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    fun login(@Query("email") email: String, @Query("password") password: String): Observable<AuthModel>

    @POST("register")
    fun registration(@Query("email") email: String,
                     @Query("password") password: String,
                     @Query("name") name: String): Observable<AuthModel>

    @GET("user")
    fun getCurrentUser(@Query("api_token") token: String): Observable<UserModel>

}
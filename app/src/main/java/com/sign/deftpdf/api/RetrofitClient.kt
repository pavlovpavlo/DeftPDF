package com.sign.deftpdf.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sign.deftpdf.util.Util
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    object ServiceBuilder {
        private val interceptor: HttpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private var gson: Gson? = GsonBuilder()
                .setLenient()
                .create()

        private var client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        private val retrofit = Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(ApiService::class.java)

        fun buildService(): ApiService {
            return retrofit
        }
    }
}
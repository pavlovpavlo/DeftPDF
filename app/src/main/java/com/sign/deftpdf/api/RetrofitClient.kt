package com.sign.deftpdf.api

import com.sign.deftpdf.util.Util
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    object ServiceBuilder {
        private val interceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private var client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(Util.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)

        fun buildService(): ApiService {
            return retrofit
        }
    }
}
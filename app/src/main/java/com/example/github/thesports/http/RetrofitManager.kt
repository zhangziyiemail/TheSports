package com.example.github.thesports.http

import com.example.github.thesports.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *   Created by Lee Zhang on 10/19/20 15:21
 */
object RetrofitManager {
    // https://www.thesportsdb.com/api/v1/json/all_leagues.php
    // https://www.thesportsdb.com/api/v1/json/1/all_leagues.php
    // https://www.thesportsdb.com/api/v1/json/1/all_sports.php

    private var BASE_URL = "https://www.thesportsdb.com/"


    val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(genericOkClient())
            .build().create(ApiService::class.java)

    }

    private fun genericOkClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    // 如果是 json 格式内容则打印 json
                    if ((message.startsWith("{") && message.endsWith("}")) ||
                        (message.startsWith("[") && message.endsWith("]"))
                    )
                        LogUtils.json(message)
                    else
                        LogUtils.verbose(message)
                }
            })
        var interceptor=Interceptor{chain ->
            chain.proceed( chain.request()
                .newBuilder()
                .method(chain.request().method, chain.request().body)
                .build())
        }


        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
            .readTimeout(10_00, TimeUnit.MILLISECONDS)
            .writeTimeout(30_00, TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
            .build()

    }
}
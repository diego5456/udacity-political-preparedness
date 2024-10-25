package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

        const val API_KEY = "AIzaSyDwMuVU4-Pf4-u34Zd-tBPztmvzyTlMZXw"

        fun getClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain -> // API key interceptor first
                    val original = chain.request()
                    val url = original.url
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                    val request = original.newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request) // Proceed to the next interceptor (logging)
                }
                .build()

        }

    }
}
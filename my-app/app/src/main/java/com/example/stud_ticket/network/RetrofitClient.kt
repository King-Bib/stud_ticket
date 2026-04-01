package com.example.stud_ticket.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Note: Change this to your actual local IP if testing on a physical device.
    // 10.0.2.2 is the special alias for your host machine in the Android Emulator.
    // Использование backend.local по явному требованию
    private const val BASE_URL = "http://backend.local/api/" 

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Host", "backend.local")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(logging)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}

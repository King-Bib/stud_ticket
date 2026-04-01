package com.example.stud_ticket.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2/api/" 

    var currentIp = "192.168.1.211"
    var currentPort = "8080"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val unsafeTrustManager = object : javax.net.ssl.X509TrustManager {
        override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
    }

    private val sslContext = javax.net.ssl.SSLContext.getInstance("SSL").apply {
        init(null, arrayOf<javax.net.ssl.TrustManager>(unsafeTrustManager), java.security.SecureRandom())
    }

    private val client = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
        .hostnameVerifier { _, _ -> true }
        .addInterceptor { chain ->
            var request = chain.request()
            // Авто-переадресация локальных доменов для эмулятора
            val actualHost = when (currentIp.lowercase()) {
                "backend.local", "localhost", "127.0.0.1" -> "10.0.2.2"
                else -> currentIp
            }

            val scheme = if (currentPort == "443" || request.url.scheme == "https") "https" else "http"
            val newUrl = request.url.newBuilder()
                .scheme(scheme)
                .host(actualHost)
                .port(currentPort.toIntOrNull() ?: 80)
                .build()

            request = request.newBuilder()
                .url(newUrl)
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

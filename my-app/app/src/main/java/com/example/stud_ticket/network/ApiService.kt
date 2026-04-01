package com.example.stud_ticket.network

import com.example.stud_ticket.LoginRequest
import com.example.stud_ticket.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

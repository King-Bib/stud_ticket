package com.example.stud_ticket.network

import com.example.stud_ticket.LoginRequest
import com.example.stud_ticket.LoginResponse
import com.example.stud_ticket.UserData
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("students")
    suspend fun getStudents(): Response<List<UserData>>

    @Multipart
    @POST("students/{id}/avatar")
    suspend fun uploadAvatar(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Response<AvatarResponse>
}

data class AvatarResponse(
    @SerializedName("status") val status: String,
    @SerializedName("photoUrl") val photoUrl: String
)

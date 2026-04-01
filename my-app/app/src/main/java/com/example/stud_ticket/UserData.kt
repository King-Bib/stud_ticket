package com.example.stud_ticket

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("id") val id: Int?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("patronymic") val patronymic: String?,
    @SerializedName("faculty") val faculty: String?,
    @SerializedName("group") val group: String?,
    @SerializedName("ticketNumber") val ticketNumber: String?,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("qrCodeData") val qrCodeData: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("entryYear") val entryYear: String?
) {
    val fio: String get() = listOfNotNull(lastName, firstName, patronymic).joinToString(" ").ifEmpty { "N/A" }
}

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("token") val token: String?,
    @SerializedName("user") val user: UserData?,
    @SerializedName("message") val message: String?
)

data class LoginRequest(
    @SerializedName("barcode") val barcode: String
)

val SampleUser = UserData(
    id = 1,
    firstName = "Артём",
    lastName = "Библев",
    patronymic = "Викторович",
    faculty = "ПЭК ГГТУ",
    group = "ИСП.23А",
    ticketNumber = "12345",
    photoUrl = null,
    qrCodeData = null,
    birthDate = "01.01.2000",
    entryYear = "2023"
)

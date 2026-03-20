package com.example.stud_ticket

data class UserData(
    val fio: String,
    val group: String,
    val organization: String
)

val SampleUser = UserData(
    fio = "Библев Артём Викторович",
    group = "ИСП.23А",
    organization = "ГОУ ВО МО ПЭК ГГТУ"
)

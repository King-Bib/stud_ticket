package com.example.stud_ticket

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RealTimeText(
    fontSize: androidx.compose.ui.unit.TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = Color.White
) {
    var time by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        val timezone = TimeZone.getTimeZone("GMT+3")
        while (true) {
            val cal = Calendar.getInstance(timezone)
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                timeZone = timezone
            }.format(cal.time)
            delay(1000)
        }
    }
    
    Text(
        text = time,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color
    )
}

@Composable
fun RealDateText(
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    color: Color = Color.White.copy(alpha = 0.8f)
) {
    var date by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        while (true) {
            date = SimpleDateFormat("dd MMMM", Locale("ru")).format(Date()).uppercase()
            delay(60000) // Update once a minute is enough for date
        }
    }
    
    Text(
        text = date,
        fontSize = fontSize,
        color = color
    )
}

package com.example.stud_ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap

@Composable
fun ProfileScreen(
    navController: NavController,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    userData: UserData
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            painter = painterResource(
                                id = if (isDarkMode) android.R.drawable.ic_menu_today else android.R.drawable.ic_menu_day
                            ),
                            contentDescription = "Theme Toggle",
                            tint = Color.White
                        )
                    }
                    RealTimeText(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Pic
            Surface(
                shape = CircleShape,
                color = Color.Gray,
                modifier = Modifier.size(100.dp)
            ) {
                // In a real app, use Image(painterResource(id = R.drawable.profile_pic))
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Section Badge
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(260.dp)
            ) {
                Text(
                    "QR-Доступа", 
                    modifier = Modifier.padding(12.dp), 
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // QR Code
            val qrText = "${userData.fio}|${userData.group}|${userData.organization}"
            val qrBitmap = remember { generateQrCode(qrText, 400) }
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(156.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Info Card
            Surface(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoRow(label = "ФИО:", value = userData.fio)
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    InfoRow(label = "Группа:", value = userData.group)
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    InfoRow(label = "Организация:", value = userData.organization)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row {
       Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
       Spacer(modifier = Modifier.width(8.dp))
       Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

fun generateQrCode(text: String, size: Int): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}

package com.example.stud_ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Brightness3
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
import com.google.zxing.EncodeHintType
import android.graphics.Bitmap
import java.util.EnumMap

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.stud_ticket.network.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

import android.widget.Toast

@Composable
fun ProfileScreen(
    navController: NavController,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    userData: UserData,
    onUserUpdate: (UserData) -> Unit,
    onSaveLocalPhoto: (String, String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            scope.launch {
                isUploading = true
                try {
                    val file = uriToFile(context, it)
                    val localPath = file.absolutePath
                    
                    // Save locally instead of server
                    onSaveLocalPhoto(userData.id.toString(), localPath)
                    onUserUpdate(userData.copy(photoUrl = localPath))
                    
                    Toast.makeText(context, "Фото успешно обновлено", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Ошибка сохранения: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isUploading = false
                }
            }
        }
    }

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
                            imageVector = if (isDarkMode) Icons.Default.WbSunny else Icons.Default.Brightness3,
                            contentDescription = "Theme Toggle",
                            tint = Color.White
                        )
                    }
                    RealTimeText(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Pic (Click to change)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { if (!isUploading) launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (userData.photoUrl != null) {
                    AsyncImage(
                        model = userData.photoUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Icon(painterResource(id = android.R.drawable.ic_menu_camera), contentDescription = "Pick Image", tint = Color.White)
                }
                
                if (isUploading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                }
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

            // QR Code with interaction
            // Simplified format for better scanner compatibility
            val qrText = "${userData.lastName}|${userData.firstName}|${userData.ticketNumber}"
            val qrBitmap = remember(qrText) { generateQrCode(qrText, 400) }
            var showInfo by remember { mutableStateOf(false) }

            Box(contentAlignment = Alignment.Center) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { showInfo = !showInfo }
                ) {
                    if (qrBitmap != null) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(156.dp)
                        )
                    }
                }

                if (showInfo) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Данные кода:", color = Color.Gray, fontSize = 10.sp)
                            Text(userData.fio, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            Text(userData.ticketNumber ?: "N/A", color = Color.White, fontSize = 12.sp)
                        }
                    }
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
                    InfoRow(label = "Группа:", value = userData.group ?: "N/A")
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    InfoRow(label = "Код специальности:", value = userData.faculty ?: "N/A")
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    InfoRow(label = "Номер студенческого билета:", value = userData.ticketNumber ?: "N/A")
                }
            }
        }
    }
}

private fun uriToFile(context: android.content.Context, uri: android.net.Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "temp_avatar_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(tempFile)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return tempFile
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
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
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

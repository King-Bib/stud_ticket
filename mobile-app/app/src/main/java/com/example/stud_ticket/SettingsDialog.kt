package com.example.stud_ticket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.stud_ticket.ui.theme.SuccessGreen
import com.example.stud_ticket.ui.theme.ErrorRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val scope = rememberCoroutineScope()

    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf("Проверить подключение") }
    var statusColor by remember { mutableStateOf(Color.Unspecified) }

    val savedIp by dataStoreManager.serverIp.collectAsState(initial = "")
    val savedPort by dataStoreManager.serverPort.collectAsState(initial = "")

    LaunchedEffect(savedIp, savedPort) {
        ip = savedIp ?: ""
        port = savedPort ?: ""
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Настройки сервера", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("IP адрес", color = Color.White.copy(alpha = 0.7f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Порт", color = Color.White.copy(alpha = 0.7f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        scope.launch {
                            statusText = "Проверка..."
                            delay(1500)
                            if (ip.isNotEmpty() && port.isNotEmpty()) {
                                statusText = "Успешно"
                                statusColor = SuccessGreen
                                dataStoreManager.saveSettings(ip, port)
                                delay(1000)
                                onDismiss()
                            } else {
                                statusText = "Ошибка"
                                statusColor = ErrorRed
                                delay(2000)
                                statusText = "Проверить подключение"
                                statusColor = Color.Unspecified
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (statusColor == Color.Unspecified) MaterialTheme.colorScheme.tertiary else statusColor
                    )
                ) {
                    Text(statusText, color = MaterialTheme.colorScheme.onTertiary)
                }

                TextButton(onClick = onDismiss) {
                    Text("Закрыть", color = Color.White)
                }
            }
        }
    }
}

package com.example.stud_ticket

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    private val SERVER_IP = stringPreferencesKey("server_ip")
    private val SERVER_PORT = stringPreferencesKey("server_port")

    val serverIp: Flow<String?> = context.dataStore.data.map { it[SERVER_IP] }
    val serverPort: Flow<String?> = context.dataStore.data.map { it[SERVER_PORT] }

    suspend fun saveSettings(ip: String, port: String) {
        context.dataStore.edit {
            it[SERVER_IP] = ip
            it[SERVER_PORT] = port
        }
    }
}

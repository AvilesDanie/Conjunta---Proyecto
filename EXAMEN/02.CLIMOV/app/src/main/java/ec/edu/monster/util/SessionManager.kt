package ec.edu.monster.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension para crear DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {
    
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val USER_ROL = stringPreferencesKey("user_rol")
        private val APP_TYPE = stringPreferencesKey("app_type") // BANQUITO o COMERCIALIZADORA
    }
    
    // Guardar sesión
    suspend fun saveSession(userId: String, username: String, rol: String, appType: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USERNAME] = username
            preferences[USER_ROL] = rol
            preferences[APP_TYPE] = appType
        }
    }
    
    // Obtener datos de sesión
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }
    val username: Flow<String?> = context.dataStore.data.map { it[USERNAME] }
    val userRol: Flow<String?> = context.dataStore.data.map { it[USER_ROL] }
    val appType: Flow<String?> = context.dataStore.data.map { it[APP_TYPE] }
    
    // Cerrar sesión
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    // Verificar si hay sesión activa
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] != null
    }
}

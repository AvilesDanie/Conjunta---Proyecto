package ec.edu.monster.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

// Extensión para mostrar Toast fácilmente
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// Composable para obtener Toast Helper
@Composable
fun rememberToastHelper(): ToastHelper {
    val context = LocalContext.current
    return remember { ToastHelper(context) }
}

class ToastHelper(private val context: Context) {
    fun showSuccess(message: String) {
        context.showToast("✅ $message", Toast.LENGTH_SHORT)
    }
    
    fun showError(message: String) {
        context.showToast("❌ $message", Toast.LENGTH_LONG)
    }
    
    fun showInfo(message: String) {
        context.showToast("ℹ️ $message", Toast.LENGTH_SHORT)
    }
    
    fun show(message: String) {
        context.showToast(message, Toast.LENGTH_SHORT)
    }
}

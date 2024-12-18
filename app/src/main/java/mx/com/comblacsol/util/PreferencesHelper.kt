package mx.com.comblacsol.util

import android.content.Context
import mx.com.comblacsol.R

class PreferencesHelper(private val context: Context) {

    private val prefs = context.getSharedPreferences(context.getString(R.string.spref), Context.MODE_PRIVATE)

    fun saveToken(token: String, expirationTime: Long) {
        prefs.edit()
            .putString("token", token)
            .putLong("token_expiration", expirationTime)
            .apply()
    }

    fun getToken(): String? = prefs.getString("token", null)

    fun getExpirationTime(): Long = prefs.getLong("token_expiration", 0)
}
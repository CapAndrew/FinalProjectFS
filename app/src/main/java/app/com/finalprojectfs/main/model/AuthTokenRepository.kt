package app.com.finalprojectfs.main.model

import android.content.Context

class AuthTokenRepository(context: Context?) {

    companion object {

        private const val PREF_NAME = "AUTH_PREFS"
        private const val AUTH_TOKEN = "AUTH_TOKEN"
    }

    private val sharedPrefs = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getAuthToken(): String = sharedPrefs?.getString(AUTH_TOKEN, null).orEmpty()

    fun setAuthToken(authToken: String) {
        sharedPrefs?.edit()
            ?.putString(AUTH_TOKEN, authToken)
            ?.apply()
    }

    fun clearAuthToken() {
        sharedPrefs?.edit()
            ?.remove(AUTH_TOKEN)
            ?.apply()
    }
}
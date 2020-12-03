package app.com.finalprojectfs.login.model

import java.io.IOException

class LoginAdapter {

    fun login(login: String, password: String): Result<String> =
        try {
            val token = POSTLogin(login, password)
            Result.Success(token)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }

    private fun POSTLogin(login: String, password: String): String {


        return "Bearer token"
    }
}
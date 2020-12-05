package app.com.finalprojectfs.login.model

import java.io.IOException

class LoginAdapter {

    fun login(login: String, password: String): Result<String> =
        try {
            val token = postLogin(login, password)
            Result.Success(token)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }

    private fun postLogin(login: String, password: String): String {

//Thread.sleep(1000)
        return "Bearer token"
    }
}
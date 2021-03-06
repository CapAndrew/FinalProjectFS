package app.com.finalprojectfs.login.model.retrofit

import app.com.finalprojectfs.main.model.RetrofitClient

object LoginApi {
    private const val BASE_URL =
        "http://focusapp-env.eba-xm2atk2z.eu-central-1.elasticbeanstalk.com"

    val loginService: RetrofitLoginService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitLoginService::class.java)
}
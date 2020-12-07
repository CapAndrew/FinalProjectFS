package app.com.finalprojectfs.login.model.retrofit

import app.com.finalprojectfs.login.model.LoginData
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitLoginService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun postLogin(
        @Body body: LoginData?,
    ): Flowable<String>
}
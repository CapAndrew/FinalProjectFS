package app.com.finalprojectfs.login.model.retrofit

import app.com.finalprojectfs.login.model.LoginData
import app.com.finalprojectfs.login.model.RegistrationData
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitLoginService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun postLogin(
        @Body body: LoginData?,
    ): Flowable<String>

    @Headers("Content-Type: application/json")
    @POST("registration")
    fun postRegistration(
        @Body body: LoginData?
    ): Flowable<RegistrationData>
}
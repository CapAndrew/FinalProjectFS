package app.com.finalprojectfs.loan.model.retrofit

import app.com.finalprojectfs.main.model.RetrofitClient

object NewLoanApi {
    private const val BASE_URL =
        "http://focusapp-env.eba-xm2atk2z.eu-central-1.elasticbeanstalk.com"

    val newLoanService: RetrofitNewLoanService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitNewLoanService::class.java)
}
package app.com.finalprojectfs.details.model.retrofit

import app.com.finalprojectfs.history.model.retrofit.RetrofitHistoryService
import app.com.finalprojectfs.main.model.RetrofitClient

object LoanDetailsApi {
    private const val BASE_URL = "http://focusapp-env.eba-xm2atk2z.eu-central-1.elasticbeanstalk.com"

    val loanDetailsService: RetrofitLoanDetailsService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitLoanDetailsService::class.java)
}
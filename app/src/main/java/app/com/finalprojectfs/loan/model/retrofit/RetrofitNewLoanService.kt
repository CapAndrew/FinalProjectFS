package app.com.finalprojectfs.loan.model.retrofit

import app.com.finalprojectfs.loan.model.entity.LoanConditionsData
import app.com.finalprojectfs.loan.model.entity.NewLoanData
import app.com.finalprojectfs.main.model.entity.LoanData
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitNewLoanService {

    @GET("loans/conditions")
    fun getLoansConditions(
        @Header("Authorization") token: String
    ): Flowable<LoanConditionsData>

    @POST("loans")
    fun postLoans(
        @Header("Authorization") token: String,
        @Body body: NewLoanData?,
    ): Flowable<LoanData>
}
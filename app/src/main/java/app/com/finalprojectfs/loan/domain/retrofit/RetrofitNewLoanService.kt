package app.com.finalprojectfs.loan.domain.retrofit

import app.com.finalprojectfs.loan.domain.entity.LoanConditionsData
import app.com.finalprojectfs.loan.domain.entity.NewLoanData
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
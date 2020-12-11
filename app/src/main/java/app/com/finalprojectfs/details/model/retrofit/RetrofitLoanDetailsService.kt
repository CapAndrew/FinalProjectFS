package app.com.finalprojectfs.details.model.retrofit

import app.com.finalprojectfs.main.model.entity.LoanData
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RetrofitLoanDetailsService {

    @GET("loans/{id}")
    fun getLoansById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Flowable<LoanData>
}
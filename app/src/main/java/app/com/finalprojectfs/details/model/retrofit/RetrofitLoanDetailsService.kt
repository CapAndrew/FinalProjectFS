package app.com.finalprojectfs.details.model.retrofit

import app.com.finalprojectfs.details.model.entity.LoanDetailsData
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitLoanDetailsService {

    @GET("loans/{id}")
    fun getLoansById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Flowable<LoanDetailsData>
}
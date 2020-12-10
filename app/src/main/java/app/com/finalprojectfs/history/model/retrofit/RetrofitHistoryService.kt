package app.com.finalprojectfs.history.model.retrofit

import app.com.finalprojectfs.history.model.entity.LoansHistoryData
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitHistoryService {

    @GET("loans/all")
    fun getLoansAll(
        @Header("Authorization") token: String
    ): Flowable<MutableList<LoansHistoryData>>
}
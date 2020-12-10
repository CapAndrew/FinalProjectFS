package app.com.finalprojectfs.history.model.retrofit

import app.com.finalprojectfs.main.model.RetrofitClient

object HistoryApi {
    private val BASE_URL = "http://focusapp-env.eba-xm2atk2z.eu-central-1.elasticbeanstalk.com"

    val historyService: RetrofitHistoryService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitHistoryService::class.java)
}
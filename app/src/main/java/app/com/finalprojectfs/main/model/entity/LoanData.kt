package app.com.finalprojectfs.main.model.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class LoanData(
    @SerializedName("amount")
    @Expose
    var amount: Int? = null,

    @SerializedName("date")
    @Expose
    var date: Date? = null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("id")
    @Expose
    var id: Long? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("percent")
    @Expose
    var percent: Double? = null,

    @SerializedName("period")
    @Expose
    var period: Int? = null,

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("state")
    @Expose
    var state: String? = null
)
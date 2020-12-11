package app.com.finalprojectfs.loan.model.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewLoanData(
    @SerializedName("amount")
    @Expose
    var amount: Int? = null,

    @SerializedName("percent")
    @Expose
    var percent: Double? = null,

    @SerializedName("period")
    @Expose
    var period: Int? = null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null
)

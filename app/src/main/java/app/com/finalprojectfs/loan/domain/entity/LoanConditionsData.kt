package app.com.finalprojectfs.loan.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoanConditionsData(
    @SerializedName("maxAmount")
    @Expose
    var maxAmount: Int? = null,

    @SerializedName("percent")
    @Expose
    var percent: Double? = null,

    @SerializedName("period")
    @Expose
    var period: Int? = null
)

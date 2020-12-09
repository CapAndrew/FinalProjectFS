package app.com.finalprojectfs.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegistrationData(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null
)

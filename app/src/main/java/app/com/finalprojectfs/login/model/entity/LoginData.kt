package app.com.finalprojectfs.login.model.entity

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null
)

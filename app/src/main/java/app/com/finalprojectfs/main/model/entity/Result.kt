package app.com.finalprojectfs.main.model.entity

sealed class Result(open val data: Any) {

    data class Success(override val data: Any) : Result(data)
    data class Error(override val data: Any) : Result(data)
}
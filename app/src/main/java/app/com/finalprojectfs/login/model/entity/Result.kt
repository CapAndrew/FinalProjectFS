package app.com.finalprojectfs.login.model.entity

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> data.toString()
            is Error -> "Error[exception=$exception]"
        }
    }
}
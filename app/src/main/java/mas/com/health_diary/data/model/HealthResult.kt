package mas.com.health_diary.data.model

sealed class HealthResult {
    data class Success<out T>(val data: T) : HealthResult()
    data class Error(val error: Throwable) : HealthResult()
}

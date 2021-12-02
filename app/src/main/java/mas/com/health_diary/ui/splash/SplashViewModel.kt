package mas.com.health_diary.ui.splash

import kotlinx.coroutines.launch
import mas.com.health_diary.data.Repository
import mas.com.health_diary.data.errors.NoAuthException
import mas.com.health_diary.ui.base.BaseViewModel

class SplashViewModel(val HealthsRepository: Repository) : BaseViewModel<Boolean?>() {
    fun requestUser() = launch {
        HealthsRepository.getCurrentUser()?.let {
            setData(true)
        } ?: let {
            setError(NoAuthException())
        }
    }
}

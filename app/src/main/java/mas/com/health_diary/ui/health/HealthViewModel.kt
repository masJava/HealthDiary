package mas.com.health_diary.ui.health

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import mas.com.health_diary.data.Repository
import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.ui.base.BaseViewModel

class HealthViewModel(val healthRepository: Repository) : BaseViewModel<HealthData>() {

    private val pendingHealth: Health?
        get() = getViewState().poll()?.health


    fun save(health: Health) {
        setData(HealthData(health = health))
    }

    fun loadHealth(healthId: String) = launch {
        try {
            healthRepository.getDataById(healthId).let {
                setData(HealthData(health = it))
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    fun deleteHealth() = launch {
        try {
            pendingHealth?.let { healthRepository.deleteData(it.id) }
            setData(HealthData(isDeleted = true))
        } catch (e: Throwable) {
            setError(e)
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingHealth?.let {
                healthRepository.saveData(it)
            }
        }
    }

}

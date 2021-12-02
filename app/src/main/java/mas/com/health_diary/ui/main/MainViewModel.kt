package mas.com.health_diary.ui.main

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import mas.com.health_diary.data.Repository
import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.data.model.HealthResult
import mas.com.health_diary.ui.base.BaseViewModel

class MainViewModel(healthRepository: Repository) : BaseViewModel<List<Health>?>() {

    private val healthChannel = healthRepository.getData()

    init {
        launch {
            healthChannel.consumeEach {
                when (it) {
                    is HealthResult.Success<*> -> setData(it.data as? List<Health>)
                    is HealthResult.Error -> setError(it.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        healthChannel.cancel()
        super.onCleared()
    }
}

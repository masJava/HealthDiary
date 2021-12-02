package mas.com.health_diary.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.data.entity.User
import mas.com.health_diary.data.model.HealthResult

interface RemoteDataProvider {

    fun subscribeToAllData(): ReceiveChannel<HealthResult>
    suspend fun getDataById(id: String): Health
    suspend fun saveData(health: Health): Health
    suspend fun getCurrentUser(): User?
    suspend fun deleteData(healthId: String)
}

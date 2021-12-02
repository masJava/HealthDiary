package mas.com.health_diary.data

import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.data.provider.RemoteDataProvider

class Repository(val remoteProvider: RemoteDataProvider) {

    fun getData() = remoteProvider.subscribeToAllData()
    suspend fun saveData(health: Health) = remoteProvider.saveData(health)
    suspend fun getDataById(id: String) = remoteProvider.getDataById(id)
    suspend fun deleteData(id: String) = remoteProvider.deleteData(id)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
}
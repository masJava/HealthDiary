package mas.com.health_diary.ui.health

import mas.com.health_diary.data.entity.Health

data class HealthData(val isDeleted: Boolean = false, val health: Health? = null)
package mas.com.health_diary.ui.main

import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.ui.base.BaseViewState


class MainViewState(health: List<Health>? = null, error: Throwable? = null) :
    BaseViewState<List<Health>?>(health, error)

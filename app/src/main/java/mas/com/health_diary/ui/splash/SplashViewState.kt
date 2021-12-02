package mas.com.health_diary.ui.splash

import mas.com.health_diary.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null) :
    BaseViewState<Boolean?>(authenticated, error)
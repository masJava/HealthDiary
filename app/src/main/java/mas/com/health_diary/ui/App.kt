package mas.com.health_diary.ui

import android.app.Application
import mas.com.health_diary.data.di.appModule
import mas.com.health_diary.data.di.healthModule
import mas.com.health_diary.data.di.mainModule
import mas.com.health_diary.data.di.splashModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, healthModule))
    }

}
package mas.com.health_diary.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mas.com.health_diary.data.Repository
import mas.com.health_diary.data.provider.FireStoreProvider
import mas.com.health_diary.data.provider.RemoteDataProvider
import mas.com.health_diary.ui.health.HealthViewModel
import mas.com.health_diary.ui.main.MainViewModel
import mas.com.health_diary.ui.splash.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val healthModule = module {
    viewModel { HealthViewModel(get()) }
}
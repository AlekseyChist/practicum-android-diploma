package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.ExternalNavigatorImpl
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitClient
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.data.network.api.VacancyApi
import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.data.repository.FiltersRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacancyRepository
import ru.practicum.android.diploma.data.storage.LocalStorage
import ru.practicum.android.diploma.data.storage.impl.LocalStorageImpl
import ru.practicum.android.diploma.domain.api.ExternalNavigator
import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.api.GetVacancyDetailsUseCase
import ru.practicum.android.diploma.domain.impl.GetVacancyDetailsUseCaseImpl

/**
 * Koin модуль для слоя данных
 * Регистрируем все компоненты здесь
 */
val dataModule = module {

    // LocalStorage (SharedPreferences)
    single<LocalStorage> {
        LocalStorageImpl(context = androidContext())
    }

    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single {
        get<AppDatabase>().vacancyDao()
    }

    // NetworkClient
    single {
        NetworkClient(context = androidContext())
    }

    // Retrofit API
    single<VacancyApi> {
        RetrofitClient.createService(VacancyApi::class.java)
    }

    // Data Source
    single {
        VacancyNetworkDataSource(
            api = get(),
            networkClient = get()
        )
    }

    // Repository для избранного
    single {
        FavoritesRepository(
            vacancyDao = get()
        )
    }

    // Repository для вакансий
    single {
        VacancyRepository(
            networkDataSource = get()
        )
    }

    // Repository для фильтров
    single<FiltersRepository> {
        FiltersRepositoryImpl(
            networkDataSource = get()
        )
    }

    // Use Case для получения деталей вакансии
    single<GetVacancyDetailsUseCase> {
        GetVacancyDetailsUseCaseImpl(
            vacancyRepository = get()
        )
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

}

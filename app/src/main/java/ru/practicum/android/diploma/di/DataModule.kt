package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitClient
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.data.network.api.VacancyApi
import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.data.storage.LocalStorage
import ru.practicum.android.diploma.data.storage.impl.LocalStorageImpl

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
            .fallbackToDestructiveMigration() // При изменении схемы - пересоздать БД
            .build()
    }

    // DAO
    // get<AppDatabase>() - Koin сам найдёт и передаст базу данных
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
    // vacancyDao = get() - Koin найдёт DAO и передаст его в конструктор
    single {
        FavoritesRepository(
            vacancyDao = get()
        )
    }
}

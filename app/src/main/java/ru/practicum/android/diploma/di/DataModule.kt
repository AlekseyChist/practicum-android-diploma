package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitClient
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.data.network.api.VacancyApi

val dataModule = module {

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
}

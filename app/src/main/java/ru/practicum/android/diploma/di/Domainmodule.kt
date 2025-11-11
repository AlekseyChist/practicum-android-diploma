package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.AddVacancyToFavoritesUseCase
import ru.practicum.android.diploma.domain.api.CheckIfVacancyFavoriteUseCase
import ru.practicum.android.diploma.domain.api.ClearFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.GetFavoriteVacanciesUseCase
import ru.practicum.android.diploma.domain.api.GetFavoriteVacancyByIdUseCase
import ru.practicum.android.diploma.domain.api.GetFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.GetIndustriesUseCase
import ru.practicum.android.diploma.domain.api.GetVacancyDetailsUseCase
import ru.practicum.android.diploma.domain.api.RemoveVacancyFromFavoritesUseCase
import ru.practicum.android.diploma.domain.api.SaveFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.SearchVacanciesUseCase
import ru.practicum.android.diploma.domain.impl.AddVacancyToFavoritesUseCaseImpl
import ru.practicum.android.diploma.domain.impl.CheckIfVacancyFavoriteUseCaseImpl
import ru.practicum.android.diploma.domain.impl.ClearFilterSettingsUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetFavoriteVacanciesUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetFavoriteVacancyByIdUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetFilterSettingsUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetIndustriesUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetVacancyDetailsUseCaseImpl
import ru.practicum.android.diploma.domain.impl.RemoveVacancyFromFavoritesUseCaseImpl
import ru.practicum.android.diploma.domain.impl.SaveFilterSettingsUseCaseImpl
import ru.practicum.android.diploma.domain.impl.SearchVacanciesUseCaseImpl

/**
 * Koin модуль для Domain слоя (UseCases)
 */
val domainModule = module {

    single<SearchVacanciesUseCase> {
        SearchVacanciesUseCaseImpl(
            vacancyRepository = get()
        )
    }

    single<GetVacancyDetailsUseCase> {
        GetVacancyDetailsUseCaseImpl(
            vacancyRepository = get()
        )
    }

    single<AddVacancyToFavoritesUseCase> {
        AddVacancyToFavoritesUseCaseImpl(
            favoritesRepository = get()
        )
    }

    single<RemoveVacancyFromFavoritesUseCase> {
        RemoveVacancyFromFavoritesUseCaseImpl(
            favoritesRepository = get()
        )
    }

    single<GetFavoriteVacanciesUseCase> {
        GetFavoriteVacanciesUseCaseImpl(
            favoritesRepository = get()
        )
    }

    single<GetFavoriteVacancyByIdUseCase> {
        GetFavoriteVacancyByIdUseCaseImpl(
            favoritesRepository = get()
        )
    }

    single<CheckIfVacancyFavoriteUseCase> {
        CheckIfVacancyFavoriteUseCaseImpl(
            favoritesRepository = get()
        )
    }

    single<GetIndustriesUseCase> {
        GetIndustriesUseCaseImpl(
            filtersRepository = get()
        )
    }

    single<SaveFilterSettingsUseCase> {
        SaveFilterSettingsUseCaseImpl(
            filterSettingsRepository = get()
        )
    }

    single<GetFilterSettingsUseCase> {
        GetFilterSettingsUseCaseImpl(
            filterSettingsRepository = get()
        )
    }

    single<ClearFilterSettingsUseCase> {
        ClearFilterSettingsUseCaseImpl(
            filterSettingsRepository = get()
        )
    }
}

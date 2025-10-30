package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.AddVacancyToFavoritesUseCase
import ru.practicum.android.diploma.domain.api.CheckIfVacancyFavoriteUseCase
import ru.practicum.android.diploma.domain.api.GetFavoriteVacanciesUseCase
import ru.practicum.android.diploma.domain.api.GetFavoriteVacancyByIdUseCase
import ru.practicum.android.diploma.domain.api.GetVacancyDetailsUseCase
import ru.practicum.android.diploma.domain.api.RemoveVacancyFromFavoritesUseCase
import ru.practicum.android.diploma.domain.impl.AddVacancyToFavoritesUseCaseImpl
import ru.practicum.android.diploma.domain.impl.CheckIfVacancyFavoriteUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetFavoriteVacanciesUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetFavoriteVacancyByIdUseCaseImpl
import ru.practicum.android.diploma.domain.impl.GetVacancyDetailsUseCaseImpl
import ru.practicum.android.diploma.domain.impl.RemoveVacancyFromFavoritesUseCaseImpl

/**
 * Koin модуль для Domain слоя (UseCases)
 */
val domainModule = module {

    // Epic 2: Детали вакансии
    single<GetVacancyDetailsUseCase> {
        GetVacancyDetailsUseCaseImpl(
            vacancyRepository = get()
        )
    }

    // Epic 3: Избранное
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
}

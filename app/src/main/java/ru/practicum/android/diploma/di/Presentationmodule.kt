package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailViewModel

/**
 * Koin модуль для Presentation слоя (ViewModels)
 */
val presentationModule = module {

    // ViewModel для экрана деталей вакансии
    viewModel {
        VacancyDetailViewModel(
            getVacancyDetailsUseCase = get(),
        )
    }

    // ViewModel для экрана списка избранного
    viewModel {
        FavoritesViewModel(
            getFavoriteVacanciesUseCase = get(),
            removeVacancyFromFavoritesUseCase = get()
        )
    }
}

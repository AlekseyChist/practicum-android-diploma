package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.filters.FiltersSettingsViewModel
import ru.practicum.android.diploma.presentation.filters.IndustryViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailViewModel

/**
 * Koin модуль для Presentation слоя (ViewModels)
 * Обновлён для поддержки функционала избранного
 */
val presentationModule = module {

    // ViewModel для экрана деталей вакансии
    viewModel {
        VacancyDetailViewModel(
            getVacancyDetailsUseCase = get(),
            addVacancyToFavoritesUseCase = get(),
            removeVacancyFromFavoritesUseCase = get(),
            checkIfVacancyFavoriteUseCase = get(),
            getFavoriteVacancyByIdUseCase = get(),
            navigator = get(),
            connectivityChecker = get()
        )
    }

    // ViewModel для экрана списка избранного
    viewModel {
        FavoritesViewModel(
            getFavoriteVacanciesUseCase = get(),
            removeVacancyFromFavoritesUseCase = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchVacanciesUseCase = get(),
            getFilterSettingsUseCase = get()
        )
    }

    // ViewModel для экрана настроек фильтров
    viewModel {
        FiltersSettingsViewModel(
            saveFilterSettings = get(),
            getFilterSettings = get(),
            clearFilterSettings = get(),
            getIndustries = get()
        )
    }

    // ViewModel для экрана выбора отрасли
    viewModel {
        IndustryViewModel(
            getIndustries = get()
        )
    }

}

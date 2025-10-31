package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailViewModel

/**
 * Koin модуль для Presentation слоя (ViewModels)
 * Обновлён для поддержки функционала избранного
 */
val presentationModule = module {

    // ViewModel для экрана деталей вакансии
    // Теперь с поддержкой добавления/удаления из избранного
    viewModel {
        VacancyDetailViewModel(
            getVacancyDetailsUseCase = get(),
            addVacancyToFavoritesUseCase = get(),
            removeVacancyFromFavoritesUseCase = get(),
            checkIfVacancyFavoriteUseCase = get()
        )
    }
}

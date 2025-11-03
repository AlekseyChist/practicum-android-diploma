package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.SearchVacanciesUseCase
import ru.practicum.android.diploma.domain.impl.SearchVacanciesUseCaseImpl
import ru.practicum.android.diploma.presentation.search.SearchViewModel

val searchModule = module {

    // ViewModel
    viewModel { SearchViewModel(get()) }

    // UseCase
    factory<SearchVacanciesUseCase> { SearchVacanciesUseCaseImpl(get()) }

}

package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.GetFavoriteVacanciesUseCase
import ru.practicum.android.diploma.domain.api.RemoveVacancyFromFavoritesUseCase

/**
 * ViewModel для экрана списка избранных вакансий
 */
class FavoritesViewModel(
    private val getFavoriteVacanciesUseCase: GetFavoriteVacanciesUseCase,
    private val removeVacancyFromFavoritesUseCase: RemoveVacancyFromFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.Initial)
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    /**
     * Загрузить список избранных вакансий
     * Подписывается на Flow из UseCase - автоматически обновляется при изменениях
     */
    private fun loadFavorites() {
        _state.value = FavoritesState.Loading

        viewModelScope.launch {
            getFavoriteVacanciesUseCase.execute()
                .catch { exception ->
                    _state.value = FavoritesState.Error(
                        exception.message ?: "Ошибка при загрузке избранного"
                    )
                }
                .collect { vacancies ->
                    _state.value = if (vacancies.isEmpty()) {
                        FavoritesState.Empty
                    } else {
                        FavoritesState.Content(vacancies)
                    }
                }
        }
    }

    /**
     * Удалить вакансию из избранного
     * @param vacancyId - идентификатор вакансии
     */
    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            removeVacancyFromFavoritesUseCase.execute(vacancyId)
                .onFailure { exception ->
                    // Логируем ошибку, но не меняем состояние
                    // Flow автоматически обновится после удаления
                    android.util.Log.e(
                        "FavoritesViewModel",
                        "Error removing vacancy: $vacancyId",
                        exception
                    )
                }
        }
    }
}

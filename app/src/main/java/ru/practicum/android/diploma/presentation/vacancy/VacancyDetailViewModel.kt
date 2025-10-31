package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.AddVacancyToFavoritesUseCase
import ru.practicum.android.diploma.domain.api.CheckIfVacancyFavoriteUseCase
import ru.practicum.android.diploma.domain.api.GetVacancyDetailsUseCase
import ru.practicum.android.diploma.domain.api.RemoveVacancyFromFavoritesUseCase
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * ViewModel для экрана деталей вакансии
 * Обновлена для поддержки функционала избранного
 */
class VacancyDetailViewModel(
    private val getVacancyDetailsUseCase: GetVacancyDetailsUseCase,
    private val addVacancyToFavoritesUseCase: AddVacancyToFavoritesUseCase,
    private val removeVacancyFromFavoritesUseCase: RemoveVacancyFromFavoritesUseCase,
    private val checkIfVacancyFavoriteUseCase: CheckIfVacancyFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyDetailState>(VacancyDetailState.Initial)
    val state: StateFlow<VacancyDetailState> = _state.asStateFlow()

    // Текущая загруженная вакансия (для добавления/удаления из избранного)
    private var currentVacancy: Vacancy? = null

    /**
     * Загрузить детальную информацию о вакансии
     * @param vacancyId - идентификатор вакансии
     */
    fun loadVacancy(vacancyId: String) {
        if (vacancyId.isBlank()) {
            _state.value = VacancyDetailState.Error("Некорректный ID вакансии")
            return
        }

        _state.value = VacancyDetailState.Loading

        viewModelScope.launch {
            // Загружаем вакансию
            getVacancyDetailsUseCase.execute(vacancyId)
                .onSuccess { vacancy ->
                    currentVacancy = vacancy

                    // Проверяем, находится ли вакансия в избранном
                    val isFavorite = checkIfVacancyFavoriteUseCase.execute(vacancyId)

                    _state.value = VacancyDetailState.Success(
                        vacancy = vacancy,
                        isFavorite = isFavorite
                    )
                }
                .onFailure { exception ->
                    handleError(exception)
                }
        }
    }

    /**
     * Переключить статус избранного для текущей вакансии
     * Добавляет в избранное, если не добавлена
     * Удаляет из избранного, если уже добавлена
     */
    fun toggleFavorite() {
        val vacancy = currentVacancy ?: return
        val currentState = _state.value as? VacancyDetailState.Success ?: return

        viewModelScope.launch {
            if (currentState.isFavorite) {
                // Удаляем из избранного
                removeVacancyFromFavoritesUseCase.execute(vacancy.id)
                    .onSuccess {
                        _state.value = currentState.copy(isFavorite = false)
                    }
            } else {
                // Добавляем в избранное
                addVacancyToFavoritesUseCase.execute(vacancy)
                    .onSuccess {
                        _state.value = currentState.copy(isFavorite = true)
                    }
            }
        }
    }

    /**
     * Повторить загрузку после ошибки
     */
    fun retry(vacancyId: String) {
        loadVacancy(vacancyId)
    }

    /**
     * Обработка ошибок
     */
    private fun handleError(exception: Throwable) {
        val message = exception.message ?: "Неизвестная ошибка"

        _state.value = when {
            message.contains("интернет", ignoreCase = true) ||
                    message.contains("connection", ignoreCase = true) -> {
                VacancyDetailState.NoConnection
            }
            else -> {
                VacancyDetailState.Error(message)
            }
        }
    }
}

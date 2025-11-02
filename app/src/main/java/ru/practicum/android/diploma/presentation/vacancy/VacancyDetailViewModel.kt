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
 */
class VacancyDetailViewModel(
    private val getVacancyDetailsUseCase: GetVacancyDetailsUseCase,
    private val addVacancyToFavoritesUseCase: AddVacancyToFavoritesUseCase,
    private val removeVacancyFromFavoritesUseCase: RemoveVacancyFromFavoritesUseCase,
    private val checkIfVacancyFavoriteUseCase: CheckIfVacancyFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyDetailState>(VacancyDetailState.Initial)
    val state: StateFlow<VacancyDetailState> = _state.asStateFlow()

    private var currentVacancy: Vacancy? = null
    /**
     * Загрузить детальную информацию о вакансии
     */
    fun loadVacancy(vacancyId: String) {
        if (vacancyId.isBlank()) {
            _state.value = VacancyDetailState.ServerError("Некорректный ID вакансии")
            return
        }

        _state.value = VacancyDetailState.Loading

        viewModelScope.launch {
            getVacancyDetailsUseCase.execute(vacancyId)
                .onSuccess { vacancy ->
                    currentVacancy = vacancy
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
     * Повторить загрузку после ошибки
     */
    fun retry(vacancyId: String) {
        loadVacancy(vacancyId)
    }

    /**
     * Переключить статус избранного для текущей вакансии
     */
    fun toggleFavorite() {
        val vacancy = currentVacancy ?: return
        val currentState = _state.value as? VacancyDetailState.Success ?: return

        viewModelScope.launch {
            if (currentState.isFavorite) {
                removeVacancyFromFavoritesUseCase.execute(vacancy.id)
                    .onSuccess {
                        _state.value = currentState.copy(isFavorite = false)
                    }
            } else {
                addVacancyToFavoritesUseCase.execute(vacancy)
                    .onSuccess {
                        _state.value = currentState.copy(isFavorite = true)
                    }
            }
        }
    }

    /**
     * Обработка ошибок с различением типов
     */
    private fun handleError(exception: Throwable) {
        val message = exception.message ?: "Неизвестная ошибка"

        _state.value = when {
            // Нет интернета
            message.contains("интернет", ignoreCase = true) || message.contains("connection", ignoreCase = true) -> {
                VacancyDetailState.NoConnection
            }

            message.contains("VACANCY_NOT_FOUND") -> {
                VacancyDetailState.NotFound
            }

            message.contains("AUTHORIZATION_ERROR") -> {
                VacancyDetailState.ServerError("Ошибка авторизации")
            }

            message.startsWith("SERVER_ERROR:") -> {
                val errorCode = message.substringAfter("SERVER_ERROR:")
                VacancyDetailState.ServerError("Ошибка сервера: $errorCode")
            }

            else -> {
                VacancyDetailState.ServerError(message)
            }
        }
    }
}

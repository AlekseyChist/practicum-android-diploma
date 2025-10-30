package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.GetVacancyDetailsUseCase

/**
 * ViewModel для экрана деталей вакансии
 */
class VacancyDetailViewModel(
    private val getVacancyDetailsUseCase: GetVacancyDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyDetailState>(VacancyDetailState.Initial)
    val state: StateFlow<VacancyDetailState> = _state.asStateFlow()

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
            getVacancyDetailsUseCase.execute(vacancyId)
                .onSuccess { vacancy ->
                    _state.value = VacancyDetailState.Success(vacancy)
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

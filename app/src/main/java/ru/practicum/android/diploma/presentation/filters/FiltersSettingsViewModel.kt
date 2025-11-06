package ru.practicum.android.diploma.presentation.filters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.ClearFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.GetFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.GetIndustriesUseCase
import ru.practicum.android.diploma.domain.api.SaveFilterSettingsUseCase
import ru.practicum.android.diploma.domain.models.FilterSettings
import ru.practicum.android.diploma.domain.models.Industry

/**
 * ViewModel для экрана настроек фильтров поиска
 * Управляет зарплатой, чекбоксом "только с ЗП", выбором отрасли
 */
class FiltersSettingsViewModel(
    private val saveFilterSettings: SaveFilterSettingsUseCase,
    private val getFilterSettings: GetFilterSettingsUseCase,
    private val clearFilterSettings: ClearFilterSettingsUseCase,
    private val getIndustries: GetIndustriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FiltersSettingsState>(FiltersSettingsState.Initial)
    val state: StateFlow<FiltersSettingsState> = _state.asStateFlow()

    // текущие значения для удобства работы (вместо копирования state постоянно)
    private var currentSalary: String = ""
    private var currentOnlyWithSalary: Boolean = false
    private var currentIndustry: Industry? = null
    private var allIndustries: List<Industry> = emptyList()

    init {
        loadSavedSettings()
    }

    /**
     * Загрузить сохраненные настройки при открытии экрана
     */
    private fun loadSavedSettings() {
        viewModelScope.launch {
            val settings = getFilterSettings.execute()

            // преобразуем Int в String для отображения
            currentSalary = settings.expectedSalary?.toString() ?: ""
            currentOnlyWithSalary = settings.onlyWithSalary
            currentIndustry = settings.industry

            updateState()
        }
    }

    /**
     * Изменить значение зарплаты
     * Принимает только цифры
     */
    fun onSalaryChanged(salary: String) {
        // фильтруем только цифры
        currentSalary = salary.filter { it.isDigit() }
        updateState()
    }

    /**
     * Очистить поле зарплаты
     */
    fun clearSalary() {
        currentSalary = ""
        updateState()
    }

    /**
     * Изменить флаг "только с зарплатой"
     */
    fun onOnlyWithSalaryChanged(enabled: Boolean) {
        currentOnlyWithSalary = enabled
        updateState()
    }

    /**
     * Установить выбранную отрасль
     * Вызывается после выбора на экране списка отраслей
     */
    fun setSelectedIndustry(industryId: Int) {
        viewModelScope.launch {
            getIndustries.execute()
                .onSuccess { industries ->
                    allIndustries = industries
                }
                .onFailure { exception ->
                    Log.d("LOG", exception.toString())
                }
            currentIndustry = allIndustries.firstOrNull { industry ->
                industry.id == industryId
            }
            updateState()
        }
    }

    /**
     * Применить настройки фильтров
     * Сохраняем в SharedPreferences через UseCase
     */
    fun applyFilters() {
        viewModelScope.launch {
            val settings = FilterSettings(
                region = null, // не используется в упрощенной версии
                industry = currentIndustry,
                expectedSalary = currentSalary.toIntOrNull(),
                onlyWithSalary = currentOnlyWithSalary
            )

            saveFilterSettings.execute(settings)

            // обновляем состояние после сохранения
            updateState()
        }
    }

    /**
     * Сбросить все настройки фильтров
     */
    fun resetFilters() {
        viewModelScope.launch {
            clearFilterSettings.execute()

            // очищаем текущие значения
            currentSalary = ""
            currentOnlyWithSalary = false
            currentIndustry = null

            updateState()
        }
    }

    /**
     * Обновить состояние экрана
     * Проверяет, есть ли активные фильтры для отображения кнопок
     */
    private fun updateState() {
        val hasActiveFilters = calculateHasActiveFilters()

        _state.value = FiltersSettingsState.Content(
            expectedSalary = currentSalary,
            onlyWithSalary = currentOnlyWithSalary,
            selectedIndustry = currentIndustry,
            hasActiveFilters = hasActiveFilters
        )
    }

    /**
     * Проверить, есть ли хотя бы один активный фильтр
     * Нужно для отображения кнопок "Применить" и "Сбросить"
     */
    private fun calculateHasActiveFilters(): Boolean {
        return currentSalary.isNotEmpty() ||
            currentOnlyWithSalary ||
            currentIndustry != null
    }
}

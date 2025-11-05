package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.storage.LocalStorage
import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.models.FilterSettings
import ru.practicum.android.diploma.domain.models.Industry

/**
 * Реализация репозитория для работы с настройками фильтров
 * Использует SharedPreferences через LocalStorage
 * Сохраняет только отрасль и параметры зарплаты
 */
class FilterSettingsRepositoryImpl(
    private val localStorage: LocalStorage
) : FilterSettingsRepository {

    override suspend fun saveSettings(settings: FilterSettings) {
        // Сохраняем индустрию
        settings.industry?.let { industry ->
            localStorage.saveInt(KEY_INDUSTRY_ID, industry.id)
            localStorage.saveString(KEY_INDUSTRY_NAME, industry.name)
        } ?: run {
            localStorage.remove(KEY_INDUSTRY_ID)
            localStorage.remove(KEY_INDUSTRY_NAME)
        }

        // Сохраняем зарплату
        settings.expectedSalary?.let { salary ->
            localStorage.saveInt(KEY_SALARY, salary)
        } ?: run {
            localStorage.remove(KEY_SALARY)
        }

        // Сохраняем флаг "только с зарплатой"
        localStorage.saveBoolean(KEY_ONLY_WITH_SALARY, settings.onlyWithSalary)
    }

    override suspend fun getSettings(): FilterSettings {
        // Восстанавливаем индустрию
        val industry = if (localStorage.contains(KEY_INDUSTRY_ID)) {
            Industry(
                id = localStorage.getInt(KEY_INDUSTRY_ID, 0),
                name = localStorage.getString(KEY_INDUSTRY_NAME, "")
            )
        } else {
            null
        }

        // Восстанавливаем зарплату
        val salary = if (localStorage.contains(KEY_SALARY)) {
            localStorage.getInt(KEY_SALARY, 0)
        } else {
            null
        }

        // Восстанавливаем флаг
        val onlyWithSalary = localStorage.getBoolean(KEY_ONLY_WITH_SALARY, false)

        return FilterSettings(
            region = null, // не используется в упрощенной версии
            industry = industry,
            expectedSalary = salary,
            onlyWithSalary = onlyWithSalary
        )
    }

    override suspend fun clearSettings() {
        localStorage.remove(KEY_INDUSTRY_ID)
        localStorage.remove(KEY_INDUSTRY_NAME)
        localStorage.remove(KEY_SALARY)
        localStorage.remove(KEY_ONLY_WITH_SALARY)
    }

    companion object {
        // ключи для хранения данных
        private const val KEY_INDUSTRY_ID = "filter_industry_id"
        private const val KEY_INDUSTRY_NAME = "filter_industry_name"
        private const val KEY_SALARY = "filter_expected_salary"
        private const val KEY_ONLY_WITH_SALARY = "filter_only_with_salary"
    }
}

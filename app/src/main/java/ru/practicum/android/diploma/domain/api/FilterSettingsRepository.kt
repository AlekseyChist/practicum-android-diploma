package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * Репозиторий для работы с настройками фильтров
 */
interface FilterSettingsRepository {

    /**
     * Сохранить настройки фильтров
     */
    suspend fun saveSettings(settings: FilterSettings)

    /**
     * Получить сохраненные настройки фильтров
     * @return настройки или пустой объект, если ничего не сохранено
     */
    suspend fun getSettings(): FilterSettings

    /**
     * Очистить все настройки фильтров
     */
    suspend fun clearSettings()
}

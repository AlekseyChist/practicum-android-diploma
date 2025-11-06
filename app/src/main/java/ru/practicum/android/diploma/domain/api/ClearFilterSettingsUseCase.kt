package ru.practicum.android.diploma.domain.api

/**
 * UseCase для очистки настроек фильтров
 */
interface ClearFilterSettingsUseCase {

    /**
     * Очистить все сохраненные настройки фильтров
     */
    suspend fun execute()
}

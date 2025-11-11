package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * UseCase для сохранения настроек фильтров
 */
interface SaveFilterSettingsUseCase {

    /**
     * Сохранить настройки фильтров в хранилище
     * @param settings настройки для сохранения
     */
    suspend fun execute(settings: FilterSettings)
}

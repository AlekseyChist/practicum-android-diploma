package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * UseCase для получения сохраненных настроек фильтров
 */
interface GetFilterSettingsUseCase {

    /**
     * Получить сохраненные настройки фильтров
     * @return настройки или пустой объект, если ничего не сохранено
     */
    suspend fun execute(): FilterSettings
}

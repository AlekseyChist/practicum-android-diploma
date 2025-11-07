package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.api.GetFilterSettingsUseCase
import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * Реализация UseCase для получения сохраненных настроек фильтров
 */
class GetFilterSettingsUseCaseImpl(
    private val filterSettingsRepository: FilterSettingsRepository
) : GetFilterSettingsUseCase {

    override suspend fun execute(): FilterSettings {
        return filterSettingsRepository.getSettings()
    }
}

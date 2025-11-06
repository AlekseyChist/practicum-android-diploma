package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.api.SaveFilterSettingsUseCase
import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * Реализация UseCase для сохранения настроек фильтров
 */
class SaveFilterSettingsUseCaseImpl(
    private val filterSettingsRepository: FilterSettingsRepository
) : SaveFilterSettingsUseCase {

    override suspend fun execute(settings: FilterSettings) {
        filterSettingsRepository.saveSettings(settings)
    }
}

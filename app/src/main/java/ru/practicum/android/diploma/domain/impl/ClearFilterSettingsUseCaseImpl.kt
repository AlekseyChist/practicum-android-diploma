package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.ClearFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.FilterSettingsRepository

/**
 * Реализация UseCase для очистки настроек фильтров
 */
class ClearFilterSettingsUseCaseImpl(
    private val filterSettingsRepository: FilterSettingsRepository
) : ClearFilterSettingsUseCase {

    override suspend fun execute() {
        filterSettingsRepository.clearSettings()
    }
}

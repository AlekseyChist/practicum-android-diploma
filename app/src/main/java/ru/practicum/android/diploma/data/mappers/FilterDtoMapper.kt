package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.responses.FilterAreaDto
import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDto
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region

object FilterDtoMapper {

    /**
     * Преобразует список регионов из DTO в список стран
     * Верхний уровень (parentId == null) считается странами
     */
    fun mapAreasToCountries(dtos: List<FilterAreaDto>): List<Country> {
        return dtos
            .filter { it.parentId == null }
            .map { mapAreaToCountry(it) }
    }

    /**
     * Преобразует один регион DTO в страну
     */
    private fun mapAreaToCountry(dto: FilterAreaDto): Country {
        return Country(
            id = dto.id,
            name = dto.name,
            regions = dto.areas?.map { mapAreaToRegion(it) } ?: emptyList()
        )
    }

    /**
     * Рекурсивно преобразует регион DTO в Region
     * Обрабатывает вложенную структуру регионов
     */
    private fun mapAreaToRegion(dto: FilterAreaDto): Region {
        return Region(
            id = dto.id,
            name = dto.name,
            parentId = dto.parentId,
            subRegions = dto.areas?.map { mapAreaToRegion(it) } ?: emptyList()
        )
    }

    /**
     * Находит регион по ID в иерархии страны
     */
    fun findRegionById(countries: List<Country>, regionId: String): Region? {
        countries.forEach { country ->
            val found = findRegionInList(country.regions, regionId)
            if (found != null) return found
        }
        return null
    }

    /**
     * Рекурсивный поиск региона в списке
     */
    private fun findRegionInList(regions: List<Region>, regionId: String): Region? {
        regions.forEach { region ->
            if (region.id == regionId) return region
            val found = findRegionInList(region.subRegions, regionId)
            if (found != null) return found
        }
        return null
    }

    /**
     * Преобразует список индустрий из DTO в domain-модели
     */
    fun mapIndustriesToDomain(dtos: List<FilterIndustryDto>): List<Industry> {
        return dtos.map { mapIndustryToDomain(it) }
    }

    /**
     * Преобразует одну индустрию из DTO
     */
    private fun mapIndustryToDomain(dto: FilterIndustryDto): Industry {
        return Industry(
            id = dto.id,
            name = dto.name
        )
    }
}

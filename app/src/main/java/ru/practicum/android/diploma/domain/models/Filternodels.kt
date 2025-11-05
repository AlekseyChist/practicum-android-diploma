package ru.practicum.android.diploma.domain.models

/**
 * Страна с регионами
 * Верхний уровень иерархии мест работы
 */
data class Country(
    val id: String,
    val name: String,
    val regions: List<Region>
)

/**
 * Регион или город
 * Может содержать вложенные регионы (например, области -> города)
 */
data class Region(
    val id: String,
    val name: String,
    val parentId: String?,
    val subRegions: List<Region>
)

/**
 * Индустрия (отрасль)
 */
data class Industry(
    val id: Int,
    val name: String
)

/**
 * Настройки фильтров поиска
 * Содержит все выбранные параметры фильтрации
 */
data class FilterSettings(
    val region: Region? = null,
    val industry: Industry? = null,
    val expectedSalary: Int? = null,
    val onlyWithSalary: Boolean = false
)

/**
 * Проверяет, есть ли активные фильтры
 * Нужно для подсветки кнопки фильтра на главном экране
 */
fun FilterSettings.hasActiveFilters(): Boolean {
    return region != null || industry != null ||
        expectedSalary != null || onlyWithSalary
}

/**
 * Преобразует настройки фильтра в параметры для API запроса
 * area передается как Int (конвертируется из String)
 */
fun FilterSettings.toApiParams(): FilterApiParams {
    return FilterApiParams(
        area = region?.id?.toIntOrNull(),
        industry = industry?.id,
        salary = expectedSalary,
        onlyWithSalary = if (onlyWithSalary) true else null
    )
}

/**
 * Параметры фильтрации для API запроса
 * Используется как промежуточная модель между FilterSettings и VacancySearchRequest
 */
data class FilterApiParams(
    val area: Int?,
    val industry: Int?,
    val salary: Int?,
    val onlyWithSalary: Boolean?
)

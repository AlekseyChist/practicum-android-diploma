package ru.practicum.android.diploma.data.storage

/**
 * Константы для ключей SharedPreferences
 */
object PreferencesKeys {

    // Ключи для фильтров поиска
    const val KEY_FILTER_COUNTRY = "filter_country"
    const val KEY_FILTER_REGION = "filter_region"
    const val KEY_FILTER_INDUSTRY = "filter_industry"
    const val KEY_FILTER_SALARY = "filter_salary"
    const val KEY_FILTER_ONLY_WITH_SALARY = "filter_only_with_salary"

    // Ключи для настроек приложения
    const val KEY_LAST_SEARCH_QUERY = "last_search_query"
    const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
}

package ru.practicum.android.diploma.domain.models

/**
 * Работодатель
 */
data class Employer(
    val id: String,
    val name: String,
    val logoUrl: String?
)

/**
 * Регион/город
 */
data class Area(
    val id: String,
    val name: String
)

/**
 * Зарплата
 */
data class Salary(
    val from: Int?,
    val to: Int?,
    val currency: String
)

/**
 * Опыт работы
 */
data class Experience(
    val id: String,
    val name: String
)

/**
 * Тип занятости
 */
data class Employment(
    val id: String,
    val name: String
)

/**
 * График работы
 */
data class Schedule(
    val id: String,
    val name: String
)

/**
 * Контактная информация
 */
data class Contacts(
    val name: String?,
    val email: String?,
    val phones: List<String>
)

// Extension functions для форматирования

/**
 * Константа для группировки цифр в числе
 */
private const val NUMBER_GROUP_SIZE = 3

/**
 * Форматирование зарплаты для отображения
 * Пример: "от 50 000 до 100 000 ₽"
 */
fun Salary.formatForDisplay(): String {
    val symbol = when (currency) {
        "RUR", "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        else -> currency
    }

    return when {
        from != null && to != null -> "от ${from.formatNumber()} до ${to.formatNumber()} $symbol"
        from != null -> "от ${from.formatNumber()} $symbol"
        to != null -> "до ${to.formatNumber()} $symbol"
        else -> "Зарплата не указана"
    }
}

/**
 * Форматирование числа с пробелами
 * Пример: 50000 -> "50 000"
 */
private fun Int.formatNumber(): String {
    return toString()
        .reversed()
        .chunked(NUMBER_GROUP_SIZE)
        .joinToString(" ")
        .reversed()
}

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
 * Зарплата с бизнес-логикой
 */
data class Salary(
    val from: Int?,
    val to: Int?,
    val currency: String
) {
    /**
     * Форматирование зарплаты для отображения
     * Пример: "от 50 000 до 100 000 ₽"
     */
    fun formatForDisplay(): String {
        val symbol = when (currency) {
            "RUR", "RUB" -> "₽"
            "USD" -> "$"
            "EUR" -> "€"
            else -> currency
        }

        return when {
            from != null && to != null -> "от ${formatNumber(from)} до ${formatNumber(to)} $symbol"
            from != null -> "от ${formatNumber(from)} $symbol"
            to != null -> "до ${formatNumber(to)} $symbol"
            else -> "Зарплата не указана"
        }
    }

    private fun formatNumber(number: Int): String {
        return number.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()
    }
}

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

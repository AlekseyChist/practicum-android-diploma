package ru.practicum.android.diploma.data.storage

/**
 * Интерфейс для работы с локальным хранилищем данных
 */
interface LocalStorage {

    // Сохранение строки
    fun saveString(key: String, value: String)

    // Получение строки
    fun getString(key: String, defaultValue: String = ""): String

    // Сохранение числа
    fun saveInt(key: String, value: Int)

    // Получение числа
    fun getInt(key: String, defaultValue: Int = 0): Int

    // Сохранение boolean
    fun saveBoolean(key: String, value: Boolean)

    // Получение boolean
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    // Удаление значения по ключу
    fun remove(key: String)

    // Очистка всех данных
    fun clear()

    // Проверка наличия ключа
    fun contains(key: String): Boolean
}

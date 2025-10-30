package ru.practicum.android.diploma.data.storage.impl

import android.content.Context
import android.content.SharedPreferences
import ru.practicum.android.diploma.data.storage.LocalStorage

/**
 * Реализация LocalStorage на основе SharedPreferences
 */
class LocalStorageImpl(context: Context) : LocalStorage {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    override fun saveString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun saveInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun remove(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    companion object {
        private const val PREFERENCES_NAME = "practicum_diploma_prefs"
    }
}

package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

/**
 * Основной класс базы данных приложения
 * Управляет созданием БД и предоставляет доступ к DAO
 */
@Database(
    entities = [VacancyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Получить DAO для работы с вакансиями
     */
    abstract fun vacancyDao(): VacancyDao

    companion object {
        const val DATABASE_NAME = "practicum_diploma_database"
    }
}

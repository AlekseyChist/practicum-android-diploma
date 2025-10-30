package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

/**
 * DAO для работы с избранными вакансиями
 */
@Dao
interface VacancyDao {

    /**
     * Добавить вакансию в избранное
     * При конфликте (вакансия уже существует) - заменяем
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    /**
     * Удалить вакансию из избранного
     */
    @Delete
    suspend fun deleteVacancy(vacancy: VacancyEntity)

    /**
     * Удалить вакансию по ID
     */
    @Query("DELETE FROM favorite_vacancies WHERE id = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: String)

    /**
     * Получить все избранные вакансии
     * Возвращает Flow для реактивного обновления UI
     */
    @Query("SELECT * FROM favorite_vacancies ORDER BY addedAt DESC")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    /**
     * Получить вакансию по ID
     */
    @Query("SELECT * FROM favorite_vacancies WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): VacancyEntity?

    /**
     * Проверить, находится ли вакансия в избранном
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancies WHERE id = :vacancyId)")
    suspend fun isVacancyFavorite(vacancyId: String): Boolean

    /**
     * Получить количество избранных вакансий
     */
    @Query("SELECT COUNT(*) FROM favorite_vacancies")
    suspend fun getVacanciesCount(): Int

    /**
     * Очистить все избранные вакансии
     */
    @Query("DELETE FROM favorite_vacancies")
    suspend fun clearAllVacancies()
}

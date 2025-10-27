package ru.practicum.android.diploma.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.mappers.VacancyEntityMapper
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Репозиторий для работы с избранными вакансиями
 * Работает ТОЛЬКО с domain моделями - не знает про DTO и Entity!
 */
class FavoritesRepository(
    private val vacancyDao: VacancyDao
) {

    /**
     * Добавить вакансию в избранное
     * Принимает domain модель, конвертирует в Entity, сохраняет в БД
     */
    suspend fun addToFavorites(vacancy: Vacancy): Result<Unit> {
        return runCatching {
            val entity = VacancyEntityMapper.mapToEntity(vacancy)
            vacancyDao.insertVacancy(entity)
        }.onFailure { exception ->
            Log.e(TAG, "Error adding vacancy to favorites: ${vacancy.id}", exception)
        }
    }

    /**
     * Удалить вакансию из избранного
     */
    suspend fun removeFromFavorites(vacancyId: String): Result<Unit> {
        return runCatching {
            vacancyDao.deleteVacancyById(vacancyId)
        }.onFailure { exception ->
            Log.e(TAG, "Error removing vacancy from favorites: $vacancyId", exception)
        }
    }

    /**
     * Получить все избранные вакансии
     * Возвращает Flow с domain моделями!
     * Flow автоматически обновляется при изменениях в БД
     */
    fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return vacancyDao.getAllVacancies()
            .map { entities ->
                entities.map { entity ->
                    VacancyEntityMapper.mapToDomain(entity)
                }
            }
            .catch { exception ->
                Log.e(TAG, "Error getting favorite vacancies", exception)
                emit(emptyList())
            }
    }

    /**
     * Получить избранную вакансию по ID
     */
    suspend fun getFavoriteVacancyById(vacancyId: String): Vacancy? {
        return runCatching {
            val entity = vacancyDao.getVacancyById(vacancyId)
            entity?.let { VacancyEntityMapper.mapToDomain(it) }
        }.onFailure { exception ->
            Log.e(TAG, "Error getting vacancy by id: $vacancyId", exception)
        }.getOrNull()
    }

    /**
     * Проверить, находится ли вакансия в избранном
     */
    suspend fun isVacancyFavorite(vacancyId: String): Boolean {
        return runCatching {
            vacancyDao.isVacancyFavorite(vacancyId)
        }.onFailure { exception ->
            Log.e(TAG, "Error checking if vacancy is favorite: $vacancyId", exception)
        }.getOrDefault(false)
    }

    /**
     * Получить количество избранных вакансий
     */
    suspend fun getFavoritesCount(): Int {
        return runCatching {
            vacancyDao.getVacanciesCount()
        }.onFailure { exception ->
            Log.e(TAG, "Error getting favorites count", exception)
        }.getOrDefault(0)
    }

    /**
     * Получить список ID всех избранных вакансий
     * Полезно для проставления флага isFavorite в списке всех вакансий
     */
    fun getFavoriteVacancyIds(): Flow<Set<String>> {
        return vacancyDao.getAllVacancies()
            .map { entities ->
                entities.map { it.id }.toSet()
            }
            .catch { exception ->
                Log.e(TAG, "Error getting favorite vacancy ids", exception)
                emit(emptySet())
            }
    }

    companion object {
        private const val TAG = "FavoritesRepository"
    }
}

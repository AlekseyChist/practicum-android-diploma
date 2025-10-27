package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
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
        return try {
            // Domain → Entity
            val entity = VacancyEntityMapper.mapToEntity(vacancy)
            vacancyDao.insertVacancy(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Удалить вакансию из избранного
     */
    suspend fun removeFromFavorites(vacancyId: String): Result<Unit> {
        return try {
            vacancyDao.deleteVacancyById(vacancyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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
                // Entity → Domain (для каждой вакансии)
                entities.map { entity ->
                    VacancyEntityMapper.mapToDomain(entity)
                }
            }
    }

    /**
     * Получить избранную вакансию по ID
     */
    suspend fun getFavoriteVacancyById(vacancyId: String): Vacancy? {
        return try {
            val entity = vacancyDao.getVacancyById(vacancyId)
            // Entity → Domain
            entity?.let { VacancyEntityMapper.mapToDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Проверить, находится ли вакансия в избранном
     */
    suspend fun isVacancyFavorite(vacancyId: String): Boolean {
        return try {
            vacancyDao.isVacancyFavorite(vacancyId)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Получить количество избранных вакансий
     */
    suspend fun getFavoritesCount(): Int {
        return try {
            vacancyDao.getVacanciesCount()
        } catch (e: Exception) {
            0
        }
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
    }
}

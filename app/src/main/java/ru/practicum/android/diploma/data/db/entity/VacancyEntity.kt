package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для хранения избранных вакансий в локальной БД
 */
@Entity(tableName = "favorite_vacancies")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val employerId: String,
    val employerName: String,
    val employerLogoUrl: String?,
    val areaId: String,
    val areaName: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val experienceId: String?,
    val experienceName: String?,
    val employmentId: String?,
    val employmentName: String?,
    val description: String,
    val keySkills: String, // Список навыков в виде строки через разделитель
    val scheduleId: String?,
    val scheduleName: String?,
    val contactsEmail: String?,
    val contactsName: String?,
    val contactsPhones: String?, // Список телефонов через разделитель
    val address: String?,
    val vacancyUrl: String,
    val alternateUrl: String?, // Альтернативный URL
    val addedAt: Long // Время добавления в избранное
)

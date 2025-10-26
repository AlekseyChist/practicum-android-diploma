package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Таблица для хранения избранных вакансий в Room БД
 * Это "плоская" структура - все вложенные объекты раскладываем в отдельные поля
 */
@Entity(tableName = "favorite_vacancies")
data class VacancyEntity(
    @PrimaryKey
    val id: String,

    // Основная информация
    val name: String,
    val description: String,
    val url: String,

    // Работодатель (раскладываем Employer)
    val employerId: String,
    val employerName: String,
    val employerLogoUrl: String?,

    // Регион (раскладываем Area)
    val areaId: String,
    val areaName: String,

    // Зарплата (раскладываем Salary)
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,

    // Опыт работы (раскладываем Experience)
    val experienceId: String?,
    val experienceName: String?,

    // Занятость (раскладываем Employment)
    val employmentId: String?,
    val employmentName: String?,

    // График (раскладываем Schedule)
    val scheduleId: String?,
    val scheduleName: String?,

    // Контакты (раскладываем Contacts)
    val contactsName: String?,
    val contactsEmail: String?,
    val contactsPhones: String?, // Список телефонов через разделитель

    // Дополнительно
    val address: String?,
    val keySkills: String, // Список навыков через разделитель

    // Метаинформация
    val addedAt: Long // Когда добавлено в избранное
)

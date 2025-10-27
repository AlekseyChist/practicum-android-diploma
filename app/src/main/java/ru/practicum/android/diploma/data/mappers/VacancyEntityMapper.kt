package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Конвертер между Domain моделью и Entity (БД)
 * Domain ↔ Entity: бизнес-логика ↔ база данных
 */
object VacancyEntityMapper {

    private const val SEPARATOR = "|||" // Разделитель для списков

    /**
     * Domain → Entity
     * Для сохранения в базу данных
     */
    fun mapToEntity(vacancy: Vacancy): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            url = vacancy.url,

            // Работодатель
            employerId = vacancy.employer.id,
            employerName = vacancy.employer.name,
            employerLogoUrl = vacancy.employer.logoUrl,

            // Регион
            areaId = vacancy.area.id,
            areaName = vacancy.area.name,

            // Зарплата
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
            salaryCurrency = vacancy.salary?.currency,

            // Опыт
            experienceId = vacancy.experience?.id,
            experienceName = vacancy.experience?.name,

            // Занятость
            employmentId = vacancy.employment?.id,
            employmentName = vacancy.employment?.name,

            // График
            scheduleId = vacancy.schedule?.id,
            scheduleName = vacancy.schedule?.name,

            // Контакты
            contactsName = vacancy.contacts?.name,
            contactsEmail = vacancy.contacts?.email,
            contactsPhones = vacancy.contacts?.phones?.joinToString(SEPARATOR),

            // Дополнительно
            address = vacancy.address,
            keySkills = vacancy.keySkills.joinToString(SEPARATOR),

            // Метаинформация
            addedAt = System.currentTimeMillis()
        )
    }

    /**
     * Entity → Domain
     * Для отображения в UI
     */
    fun mapToDomain(entity: VacancyEntity): Vacancy {
        return Vacancy(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            url = entity.url,
            employer = createEmployer(entity),
            area = createArea(entity),
            salary = createSalary(entity),
            experience = createExperience(entity),
            employment = createEmployment(entity),
            schedule = createSchedule(entity),
            contacts = createContacts(entity),
            address = entity.address,
            keySkills = parseSkills(entity.keySkills),
            isFavorite = true
        )
    }

    private fun createEmployer(entity: VacancyEntity): Employer {
        return Employer(
            id = entity.employerId,
            name = entity.employerName,
            logoUrl = entity.employerLogoUrl
        )
    }

    private fun createArea(entity: VacancyEntity): Area {
        return Area(
            id = entity.areaId,
            name = entity.areaName
        )
    }

    private fun createSalary(entity: VacancyEntity): Salary? {
        return if (entity.salaryFrom != null || entity.salaryTo != null) {
            Salary(
                from = entity.salaryFrom,
                to = entity.salaryTo,
                currency = entity.salaryCurrency ?: "RUR"
            )
        } else {
            null
        }
    }

    private fun createExperience(entity: VacancyEntity): Experience? {
        return if (entity.experienceId != null && entity.experienceName != null) {
            Experience(
                id = entity.experienceId,
                name = entity.experienceName
            )
        } else {
            null
        }
    }

    private fun createEmployment(entity: VacancyEntity): Employment? {
        return if (entity.employmentId != null && entity.employmentName != null) {
            Employment(
                id = entity.employmentId,
                name = entity.employmentName
            )
        } else {
            null
        }
    }

    private fun createSchedule(entity: VacancyEntity): Schedule? {
        return if (entity.scheduleId != null && entity.scheduleName != null) {
            Schedule(
                id = entity.scheduleId,
                name = entity.scheduleName
            )
        } else {
            null
        }
    }

    private fun createContacts(entity: VacancyEntity): Contacts? {
        val hasAnyContact = entity.contactsName != null ||
            entity.contactsEmail != null ||
            !entity.contactsPhones.isNullOrBlank()

        return if (hasAnyContact) {
            Contacts(
                name = entity.contactsName,
                email = entity.contactsEmail,
                phones = parsePhones(entity.contactsPhones)
            )
        } else {
            null
        }
    }

    /**
     * Разделить строку навыков на список
     */
    private fun parseSkills(skillsString: String): List<String> {
        if (skillsString.isBlank()) return emptyList()
        return skillsString.split(SEPARATOR).filter { it.isNotBlank() }
    }

    /**
     * Разделить строку телефонов на список
     */
    private fun parsePhones(phonesString: String?): List<String> {
        if (phonesString.isNullOrBlank()) return emptyList()
        return phonesString.split(SEPARATOR).filter { it.isNotBlank() }
    }
}

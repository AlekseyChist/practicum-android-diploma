package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.responses.*
import ru.practicum.android.diploma.domain.models.*

/**
 * Конвертер из DTO (от API) в Domain модели
 * DTO → Domain: от внешнего API к нашей бизнес-логике
 */
object VacancyDtoMapper {

    /**
     * Преобразовать полную информацию о вакансии (VacancyDetailDto) в Domain
     */
    fun mapDetailToDomain(dto: VacancyDetailDto): Vacancy {
        return Vacancy(
            id = dto.id,
            name = dto.name,
            employer = mapEmployer(dto.employer),
            area = mapArea(dto.area),
            salary = dto.salary?.let { mapSalary(it) },
            experience = dto.experience?.let { mapExperience(it) },
            employment = dto.employment?.let { mapEmployment(it) },
            schedule = dto.schedule?.let { mapSchedule(it) },
            description = dto.description,
            keySkills = dto.skills,
            contacts = dto.contacts?.let { mapContacts(it) },
            address = buildAddress(dto.address),
            url = dto.url,
            isFavorite = false // По умолчанию не в избранном (проверим потом)
        )
    }

    /**
     * Преобразовать краткую информацию о вакансии (VacancyDto) в Domain
     * Используется для списка вакансий
     */
    fun mapShortToDomain(dto: VacancyDto): Vacancy {
        return Vacancy(
            id = dto.id,
            name = dto.name,
            employer = dto.employer?.let { mapEmployer(it) }
                ?: Employer("", "Не указан", null),
            area = dto.area?.let { Area(it.id, it.name) }
                ?: Area("", "Не указан"),
            salary = dto.salary?.let { mapSalary(it) },
            experience = dto.experience?.let { mapExperience(it) },
            employment = dto.employment?.let { mapEmployment(it) },
            schedule = dto.schedule?.let { mapSchedule(it) },
            description = "",
            keySkills = emptyList(),
            contacts = null,
            address = null,
            url = "",
            isFavorite = false
        )
    }

    // Вспомогательные функции для преобразования вложенных объектов

    private fun mapEmployer(dto: EmployerDto): Employer {
        return Employer(
            id = dto.id,
            name = dto.name,
            logoUrl = dto.logo
        )
    }

    private fun mapArea(dto: FilterAreaDto): Area {
        return Area(
            id = dto.id,
            name = dto.name
        )
    }

    private fun mapSalary(dto: SalaryDto): Salary? {
        // Если и from и to null - зарплаты нет
        if (dto.from == null && dto.to == null) return null

        return Salary(
            from = dto.from,
            to = dto.to,
            currency = dto.currency ?: "RUR"
        )
    }

    private fun mapExperience(dto: ExperienceDto): Experience {
        return Experience(
            id = dto.id,
            name = dto.name
        )
    }

    private fun mapEmployment(dto: EmploymentDto): Employment {
        return Employment(
            id = dto.id,
            name = dto.name
        )
    }

    private fun mapSchedule(dto: ScheduleDto): Schedule {
        return Schedule(
            id = dto.id,
            name = dto.name
        )
    }

    private fun mapContacts(dto: ContactsDto): Contacts? {
        // Если все поля пустые - контактов нет
        if (dto.name == null && dto.email == null && dto.phone.isNullOrEmpty()) {
            return null
        }

        return Contacts(
            name = dto.name,
            email = dto.email,
            phones = dto.phone ?: emptyList()
        )
    }

    private fun buildAddress(dto: AddressDto?): String? {
        if (dto == null) return null

        // Если есть полный адрес - используем его
        if (!dto.fullAddress.isNullOrBlank()) {
            return dto.fullAddress
        }

        // Иначе собираем из частей
        val parts = listOfNotNull(
            dto.city,
            dto.street,
            dto.building
        ).filter { it.isNotBlank() }

        return parts.takeIf { it.isNotEmpty() }?.joinToString(", ")
    }
}

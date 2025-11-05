package ru.practicum.android.diploma.ui.vacancy.mock

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.Vacancy

val mockVacancy = Vacancy(
    id = "1",
    name = "Android-разработчик",
    employer = Employer(
        id = "123",
        name = "Еда",
        logoUrl = null
    ),
    area = Area(
        id = "213",
        name = "Москва"
    ),
    salary = Salary(
        from = 100_000,
        to = 200_000,
        currency = "RUR"
    ),
    experience = Experience(
        id = "exp_1",
        name = "От 1 года до 4 лет"
    ),
    employment = Employment(
        id = "emp_1",
        name = "Полная занятость"
    ),
    schedule = Schedule(
        id = "sched_1",
        name = "Удалённая работа"
    ),
    description = "Если ты начинающий DevOps-инженер и хочешь развиваться в динамично развивающейся " +
        "\n- Gitlab CI,\n- Ansible,\n- Kubernetes,\n- Nginx,\n- Vault,\n- Ansible,\n- Docker, Docker-compose.",
    keySkills = listOf("Kotlin", "Jetpack Compose", "MVVM"),
    contacts = Contacts(
        name = "HR Анна",
        email = "hr@eda.ru",
        phones = listOf("+7 (999) 123-45-67")
    ),
    address = "Москва, ул. Ленина, 10",
    url = "https://company.ru/vacancy/1",
    isFavorite = false
)

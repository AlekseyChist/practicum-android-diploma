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
//        "аккредитованной IT-компании, мы ждем тебя! Мы предоставляем высококвалифицированных специалистов " +
//        "для реализации проектов различной сложности и помогаем партнерам успешно завершать их, обеспечивая" +
//        " высокое качество услуг.\n\nЧто мы предлагаем:\n\nПолностью удаленный формат работы с гибким графиком " +
//        "(5/2 с 9:00 до 19:00).\n\nСовременные инструменты и подходы для автоматизации тестирования.\nВозможности" +
//        " для профессионального роста и обучения.\nДружелюбную атмосферу в коллективе и поддержку коллег." +
//        "\nКонкурентоспособную заработную плату, начиная от 70 тысяч рублей.\nНеоплачиваемая стажировка в " +
//        "течение 2-3 месяцев.\nОбязанности:\nСопровождать тестовые окружения\n\nПереносить сервисы в " +
//        "Kubernetes\nАвтоматизировать сборку и доставку приложений (CI/CD)\nПисать новые роли Ansible" +
//        "\nПоддерживать команды разработки и тестирования.\nОт тебя ждём:\nУмение работать с:\n\n- Jenkins," +
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

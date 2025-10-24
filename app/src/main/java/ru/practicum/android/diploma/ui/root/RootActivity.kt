package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.NetworkResult
import ru.practicum.android.diploma.data.network.RetrofitClient
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.data.network.api.VacancyApi

class RootActivity : AppCompatActivity() {

    private val TAG = "RootActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        // Тестовый запрос к API
        testApiConnection()
    }

    private fun testApiConnection() {
        lifecycleScope.launch {
            try {
                // Создаём API и NetworkClient
                val api = RetrofitClient.createService(VacancyApi::class.java)
                val networkClient = NetworkClient(applicationContext)
                val dataSource = VacancyNetworkDataSource(api, networkClient)

                // Тестируем запрос регионов
                Log.d(TAG, "🚀 Делаем запрос к API...")

                when (val result = dataSource.getAreas()) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "✅ SUCCESS! Получено регионов: ${result.data.size}")
                        result.data.take(5).forEach { area ->
                            Log.d(TAG, "  📍 ${area.name} (id: ${area.id})")
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.e(TAG, "❌ ERROR! Code: ${result.code}, Message: ${result.message}")
                    }
                    is NetworkResult.NoConnection -> {
                        Log.e(TAG, "📵 NO CONNECTION!")
                    }
                }

                // Тестируем поиск вакансий
                Log.d(TAG, "🔍 Ищем вакансии 'Android Developer'...")

                when (val result = dataSource.searchVacancies(text = "Android Developer", page = 0)) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "✅ SUCCESS! Найдено вакансий: ${result.data.found}")
                        Log.d(TAG, "📄 Страниц: ${result.data.pages}")
                        result.data.vacancies.take(3).forEach { vacancy ->
                            Log.d(TAG, "  💼 ${vacancy.name}")
                            Log.d(TAG, "     Компания: ${vacancy.employer?.name ?: "Не указана"}")
                            Log.d(TAG, "     Зарплата: ${vacancy.salary?.from ?: "?"} - ${vacancy.salary?.to ?: "?"} ${vacancy.salary?.currency ?: ""}")
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.e(TAG, "❌ ERROR! Code: ${result.code}, Message: ${result.message}")
                    }
                    is NetworkResult.NoConnection -> {
                        Log.e(TAG, "📵 NO CONNECTION!")
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 EXCEPTION: ${e.message}", e)
            }
        }
    }
}

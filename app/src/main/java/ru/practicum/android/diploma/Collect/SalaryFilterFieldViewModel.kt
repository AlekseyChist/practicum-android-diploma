package ru.practicum.android.diploma.Collect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

open class SalaryFilterFieldViewModel : ViewModel() {

    var input by mutableStateOf("")
        protected set

    fun onInputChange(newValue: String) {
        input = newValue.filter { it.isDigit() }
    }

    fun clear() {
        input = ""
    }
}

package ru.practicum.android.diploma.tempdevfolder

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel

class SalaryFilterFieldViewModel : ViewModel() {

    var input by mutableStateOf("")
        protected set

    fun onInputChange(newValue: String) {
        input = newValue.filter { it.isDigit() }
    }

    fun clear() {
        input = ""
    }
}

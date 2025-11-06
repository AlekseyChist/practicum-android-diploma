package ru.practicum.android.diploma.tempdevfolder

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.LightGray

@Composable
fun NumberInputScreen(viewModel: SalaryFilterFieldViewModel = viewModel()) {
    val value = viewModel.input
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = value,
            onValueChange = viewModel::onInputChange,
            modifier = Modifier.weight(1f),
            label = {
                Text(
                    "Ожидаемая зарплата",
                    fontSize = 12.sp
                )
            },
            placeholder = {
                Text(
                    text = "Введите сумму",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp // размер шрифта вводимого текста
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),

            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clear() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Blue,
                unfocusedLabelColor = Gray,
                cursorColor = Blue,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NumberInputScreenPreview() {
    MaterialTheme {
        NumberInputScreen(
            viewModel = object : SalaryFilterFieldViewModel() {
                init {
                    input = "1234"
                }
            }
        )
    }
}

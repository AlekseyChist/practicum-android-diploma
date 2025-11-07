package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.search.SalaryFilterFieldViewModel
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.LightGray

@Composable
fun SalaryFilterField(
    viewModel: SalaryFilterFieldViewModel = viewModel()
) {
    val salary = viewModel.input
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(Dimens.dp_16)
    ) {
        Text(
            text = stringResource(R.string.expected_salary),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isFocused) Blue else Gray,
            maxLines = 1,
            modifier = Modifier
                .offset(x = 16.dp, y = 24.dp)
                .zIndex(1f)
        )

        TextField(
            value = salary,
            maxLines = 1,
            onValueChange = viewModel::onInputChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .zIndex(0f)
                .height(72.dp),
            placeholder = { Text(stringResource(R.string.enter_amount)) },

            trailingIcon = {
                if (salary.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clear() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear_field)
                        )
                    }
                }
            },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,

            shape = RoundedCornerShape(Dimens.dp_16),

            textStyle = MaterialTheme.typography.bodyLarge,

            colors = TextFieldDefaults.colors(
                focusedPlaceholderColor = Gray,
                unfocusedPlaceholderColor = Gray,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Blue
            )
        )
    }
}

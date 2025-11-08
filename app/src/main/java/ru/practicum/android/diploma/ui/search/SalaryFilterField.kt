package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.LightGray

@Composable
fun SalaryFilterField() {
    var inputValue by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = LightGray,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(0.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
        ) {
            Text(
                text = stringResource(R.string.expected_salary),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isFocused) Blue else Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
            TextField(
                value = inputValue,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        inputValue = newValue
                    }
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_amount),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray,
                    unfocusedContainerColor = LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Blue
                )
            )
        }
        if (inputValue.isNotEmpty()) {
            IconButton(
                onClick = { inputValue = "" },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_field)
                )
            }
        }
    }
}

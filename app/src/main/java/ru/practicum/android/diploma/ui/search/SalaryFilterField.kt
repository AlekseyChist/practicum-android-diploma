package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun SalaryFilterField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onDone: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.padding_16,
                vertical = Dimens.padding_8
            )
            .height(60.dp)
            .background(
                color = colorResource(R.color.light_gray),
                shape = RoundedCornerShape(Dimens.padding_12)
            )
    ) {
        val labelColor = when {
            isFocused -> Blue
            value.isNotEmpty() && isFocused -> MaterialTheme.colorScheme.primary
            value.isNotEmpty() -> Color.Black
            isFocused -> MaterialTheme.colorScheme.primary
            else -> colorResource(R.color.color_gray_search_placeholder)
        }

        Text(
            text = stringResource(R.string.filters_salary_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            color = labelColor,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = Dimens.padding_16,
                    top = Dimens.padding_8
                )
        )

        BasicTextField(
            value = value,
            onValueChange = { entered -> onValueChange(entered.filter(Char::isDigit)) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            cursorBrush = SolidColor(Blue),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboard?.hide(); onDone() }),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(
                    start = Dimens.padding_16,
                    end = Dimens.padding_16,
                    bottom = Dimens.padding_8
                )
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.filters_salary_hint),
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(R.color.color_gray_search_placeholder)
                        )
                    }
                    innerTextField()
                }
            },
        )

        if (value.isNotEmpty()) {
            IconButton(
                onClick = {
                    onClear()
                    keyboard?.hide()
                },
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Icon(
                    painter = painterResource(R.drawable.close_icon),
                    contentDescription = stringResource(R.string.clear),
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SalaryFilterFieldPreview() {
    var text by remember { mutableStateOf("12345") }

    SalaryFilterField(
        value = text,
        onValueChange = { text = it },
        onClear = { text = "" },
        onDone = { /* действие при Done */ }
    )
}

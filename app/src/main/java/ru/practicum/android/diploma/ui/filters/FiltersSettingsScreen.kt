package ru.practicum.android.diploma.ui.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.material3.CheckboxDefaults
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersSettingsScreen(
    salaryText: String,
    onlyWithSalary: Boolean,
    industryName: String?,
    onSalaryChange: (String) -> Unit,
    onClearSalary: () -> Unit,
    onToggleOnlyWithSalary: (Boolean) -> Unit,
    onIndustryClick: () -> Unit,
    onClearIndustry: () -> Unit,
    onApplyClick: () -> Unit,
    onResetClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current

    val hasAnyFilter = remember(salaryText, onlyWithSalary, industryName) {
        salaryText.isNotBlank() || onlyWithSalary || !industryName.isNullOrBlank()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Row(modifier = Modifier.heightIn(Dimens.appBarHeight)) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.filters_settings_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    windowInsets = WindowInsets.statusBars,
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(
                    top = inner.calculateTopPadding(),
                    start = inner.calculateStartPadding(LayoutDirection.Ltr),
                    end = inner.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(Dimens.padding_36))

            IndustryRow(
                value = industryName,
                onClick = onIndustryClick,
                onClear = onClearIndustry,
            )

            Spacer(modifier = Modifier.height(Dimens.padding_24))

            SalaryField(
                value = salaryText,
                onValueChange = { digitsOnly -> onSalaryChange(digitsOnly) },
                onClear = {
                    onClearSalary()
                    keyboard?.hide()
                    focus.clearFocus()
                },
                onDone = {
                    keyboard?.hide()
                    focus.clearFocus()
                },
            )
            Spacer(modifier = Modifier.height(Dimens.padding_24))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.padding_16,
                        vertical = Dimens.padding_8
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.filters_no_salary_checkbox),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Checkbox(
                    checked = onlyWithSalary,
                    onCheckedChange = onToggleOnlyWithSalary,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            if (hasAnyFilter) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.padding_16,
                            vertical = Dimens.padding_16
                        ),
                    verticalArrangement = Arrangement.spacedBy(Dimens.padding_8)
                ) {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            focus.clearFocus()
                            onApplyClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.padding_56),
                        shape = RoundedCornerShape(Dimens.padding_16),
                    ) {
                        Text(
                            stringResource(R.string.filters_apply),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    TextButton(
                        onClick = onResetClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Dimens.padding_16)
                    ) {
                        Text(
                            text = stringResource(R.string.filters_reset),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IndustryRow(
    value: String?,
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    val isSelected = !value.isNullOrBlank()

    if (isSelected) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.padding_16,
                    end = Dimens.padding_8,
                    top = Dimens.padding_6,
                    bottom = Dimens.padding_8
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.filters_industry_title),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
                Spacer(Modifier.height(Dimens.padding_1))
                Text(
                    text = value!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onClear) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.clear),
                    tint = Color.Black
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = Dimens.padding_16,
                    vertical = Dimens.padding_8
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filters_industry_title),
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.color_gray_search_placeholder),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun SalaryField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onDone: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding_16,
                vertical = Dimens.padding_8
            )
            .height(Dimens.dp_56)
            .background(
                color = colorResource(R.color.light_gray),
                shape = RoundedCornerShape(Dimens.padding_12)
            )
    ) {
        val labelColor = when {
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
                .padding(start = Dimens.padding_16,
                    top = Dimens.padding_8
                )
        )

        BasicTextField(
            value = value,
            onValueChange = { entered -> onValueChange(entered.filter(Char::isDigit)) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboard?.hide(); onDone() }),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = Dimens.padding_16,
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
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.clear),
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(name = "Filters • Empty (Light)", showBackground = true, showSystemUi = true, device = Devices.PIXEL_5)
@Composable
fun Preview_Filters_Empty_Light() {
    AppTheme {
        FiltersSettingsScreen(
            salaryText = "",
            onlyWithSalary = false,
            industryName = null,
            onSalaryChange = {},
            onClearSalary = {},
            onToggleOnlyWithSalary = {},
            onIndustryClick = {},
            onClearIndustry = {},
            onApplyClick = {},
            onResetClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Filters • Filled (Light)", showBackground = true, showSystemUi = true, device = Devices.PIXEL_5)
@Composable
fun Preview_Filters_Filled_Light() {
    AppTheme {
        FiltersSettingsScreen(
            salaryText = "40000",
            onlyWithSalary = true,
            industryName = "IT",
            onSalaryChange = {},
            onClearSalary = {},
            onToggleOnlyWithSalary = {},
            onIndustryClick = {},
            onClearIndustry = {},
            onApplyClick = {},
            onResetClick = {},
            onBackClick = {},
        )
    }
}

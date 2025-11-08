package ru.practicum.android.diploma.ui.filters

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filters.IndustryState
import ru.practicum.android.diploma.ui.filters.mock.IndustryStateProvider
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun IndustryScreen(
    state: IndustryState,
    query: String,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    onClickIndustry: (Industry) -> Unit,
    onSelectIndustry: (Int) -> Unit
) {
    var textState by remember(query) { mutableStateOf(query) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = { TopBar(onBackClick = onBackClick) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = remember { WindowInsets(0, 0, 0, 0) },
        bottomBar = {
            if (state is IndustryState.Content && state.selectedIndustry != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(
                            start = Dimens.padding_16,
                            end = Dimens.padding_16,
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding() + Dimens.padding_24
                        )
                ) {
                    Button(
                        onClick = {
                            onSelectIndustry(state.selectedIndustry.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.size_60),
                        shape = RoundedCornerShape(Dimens.corner)
                    ) {
                        Text(
                            text = stringResource(R.string.choose),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
        ) {
            SearchIndustryField(
                value = textState,
                onValueChange = {
                    textState = it
                    onQueryChange(it)
                },
                onClear = {
                    textState = ""
                    onClearClick()
                },
                onSubmit = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onSearchClick()
                }
            )
            Spacer(Modifier.size(Dimens.padding_8))
            when (state) {
                is IndustryState.Content -> {
                    IndustryList(
                        state.filteredIndustries,
                        selectedIndustry = state.selectedIndustry,
                        onClickIndustry = { industry ->
                            onClickIndustry(industry)
                        }
                    )
                }

                is IndustryState.EmptySearch -> {
                    ErrorSection(
                        idRes = R.drawable.no_list_placeholder,
                        message = stringResource(R.string.placeholder_no_list_found)
                    )
                }

                is IndustryState.Error -> {
                    ErrorSection(
                        idRes = R.drawable.server_not_responding_placeholder,
                        message = stringResource(R.string.server_error)
                    )
                }

                is IndustryState.Initial, IndustryState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                    }
                }

                is IndustryState.NoConnection -> {
                    ErrorSection(
                        idRes = R.drawable.no_internet_placeholder,
                        message = stringResource(R.string.placeholder_no_internet)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .heightIn(Dimens.appBarHeight)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        painter = painterResource(R.drawable.nav_icon),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.select_industry),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            windowInsets = WindowInsets.statusBars
        )
    }
}

@Composable
private fun SearchIndustryField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onSubmit: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding_16, vertical = Dimens.padding_8)
            .height(Dimens.dp_56),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.indostry_hint),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colorResource(R.color.color_gray_search_placeholder)
                )
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_gray),
            unfocusedContainerColor = colorResource(R.color.light_gray),
            disabledContainerColor = colorResource(R.color.light_gray),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.Blue
        ),
        shape = RoundedCornerShape(Dimens.padding_12),
        leadingIcon = null,
        trailingIcon = {
            Row(
                modifier = Modifier.padding(end = Dimens.padding_16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClear, modifier = Modifier.size(Dimens.padding_24)) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.clear),
                            tint = Color.Black
                        )
                    }
                } else {
                    IconButton(onClick = onSubmit, modifier = Modifier.size(Dimens.padding_24)) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search_hint),
                            tint = Color.Black
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSubmit() })
    )
}

@Composable
private fun IndustryList(
    items: List<Industry>,
    selectedIndustry: Industry?,
    onClickIndustry: (Industry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { item ->
            IndustryListItem(
                text = item.name,
                selected = selectedIndustry?.id == item.id,
                onRadioButtonClick = { onClickIndustry(item) }
            )
        }
    }
}

@Composable
private fun IndustryListItem(
    text: String,
    selected: Boolean,
    onRadioButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.padding_16, end = Dimens.padding_4)
            .height(Dimens.size_60),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(Modifier.size(Dimens.padding_16))
        Box(
            modifier = Modifier
                .size(Dimens.size_48),
            contentAlignment = Alignment.Center
        ) {
            RadioButton(
                onClick = onRadioButtonClick,
                selected = selected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Blue,
                    unselectedColor = Color.Blue,
                    disabledSelectedColor = Color.Blue.copy(alpha = 0.5f),
                    disabledUnselectedColor = Color.Blue.copy(alpha = 0.5f)
                ),
            )
        }

    }
}

@Composable
fun ErrorSection(
    idRes: Int,
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.padding_16),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(idRes),
                contentDescription = "ÐžÑˆÐ¸Ð±ÐºÐ° Ð·Ð°Ð³Ñ€ÑƒÐ·ÐºÐ¸",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Dimens.padding_16))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(max = 268.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IndustryScreenPreview(
    @PreviewParameter(IndustryStateProvider::class) state: IndustryState
) {
    AppTheme {
        IndustryScreen(
            state = state,
            query = "",
            onBackClick = {},
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onClickIndustry = {},
            onSelectIndustry = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun IndustryScreenDarkPreview(
    @PreviewParameter(IndustryStateProvider::class) state: IndustryState
) {
    AppTheme {
        IndustryScreen(
            state = state,
            query = "",
            onBackClick = {},
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onClickIndustry = {},
            onSelectIndustry = {}
        )
    }
}

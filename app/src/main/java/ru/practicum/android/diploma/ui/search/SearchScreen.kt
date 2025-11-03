package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyUi
import ru.practicum.android.diploma.ui.theme.Dimens

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Typing : SearchUiState
    data object NoInternet : SearchUiState
    data object EmptyResult : SearchUiState
    data class Success(val items: List<VacancyUi>) : SearchUiState
    data class Error(val message: String? = null) : SearchUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onVacancyClick: (VacancyUi) -> Unit,
) {
    var textState by remember(query) { mutableStateOf(query) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = remember { WindowInsets(0, 0, 0, 0) },
        topBar = {
            Row(modifier = Modifier.heightIn(Dimens.appBarHeight)) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.search_job),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    actions = {
                        IconButton(onClick = onFilterClick) {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = stringResource(R.string.filters_settings),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    windowInsets = WindowInsets.statusBars
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchField(
                value = textState,
                onValueChange = {
                    textState = it
                    onQueryChange(it)
                },
                onClear = {
                    textState = ""
                    onClearClick()
                },
                onSubmit = onSearchClick
            )

            when (state) {
                SearchUiState.Idle -> Placeholder(
                    imageRes = R.drawable.search_placeholder_euy,
                    text = ""
                )

                SearchUiState.Typing -> {
                    Spacer(Modifier.height(Dimens.padding_0))
                }

                SearchUiState.Loading -> LoadingPlaceholder()

                SearchUiState.NoInternet -> NoInternetPlaceholder()

                SearchUiState.EmptyResult -> EmptyResultPlaceholder()

                is SearchUiState.Error -> ErrorPlaceholder(state.message)

                is SearchUiState.Success -> VacancyList(
                    items = state.items,
                    onItemClick = onVacancyClick
                )
            }
        }
    }
}

@Composable
private fun SearchField(
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
                text = stringResource(R.string.search_hint),
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
internal fun VacancyList(
    items: List<VacancyUi>,
    onItemClick: (VacancyUi) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Dimens.padding_16,
            end = Dimens.padding_16,
            bottom = Dimens.padding_16
        )
    ) {
        items(items, key = { it.id }) { item ->
            VacancyListItem(item = item, onClick = { onItemClick(item) })
            Divider()
        }
    }
}

/**
 * Плейсхолдер загрузки
 */
@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Плейсхолдер с картинкой и текстом
 */
@Composable
private fun Placeholder(
    imageRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.padding_16),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

/**
 * Плейсхолдер "Нет интернета"
 */
@Composable
private fun NoInternetPlaceholder() {
    Placeholder(
        imageRes = R.drawable.no_internet_placeholder,
        text = stringResource(R.string.placeholder_no_internet)
    )
}

/**
 * Плейсхолдер "Ничего не найдено  "
 */
@Composable
private fun EmptyResultPlaceholder() {
    Placeholder(
        imageRes = R.drawable.no_vacanc_placeholder,
        text = stringResource(R.string.placeholder_nothing_found)
    )
}

/**
 * Плейсхолдер "Ошибка сервера"
 */
@Composable
private fun ErrorPlaceholder(message: String? = null) {
    Placeholder(
        imageRes = R.drawable.server_not_responding_placeholder,
        text = message ?: stringResource(R.string.placeholder_error)
    )
}

@Preview(name = "Idle", showBackground = true)
@Composable
fun PreviewSearchScreenIdle() {
    SearchScreen(
        state = SearchUiState.Idle,
        query = "",
        onQueryChange = {},
        onClearClick = {},
        onSearchClick = {},
        onFilterClick = {},
        onVacancyClick = {}
    )
}

@Preview(name = "Loading", showBackground = true)
@Composable
fun PreviewSearchScreenLoading() {
    SearchScreen(
        state = SearchUiState.Loading,
        query = "Android Developer",
        onQueryChange = {},
        onClearClick = {},
        onSearchClick = {},
        onFilterClick = {},
        onVacancyClick = {}
    )
}

@Preview(name = "No Internet", showBackground = true)
@Composable
fun PreviewSearchScreenNoInternet() {
    SearchScreen(
        state = SearchUiState.NoInternet,
        query = "Designer",
        onQueryChange = {},
        onClearClick = {},
        onSearchClick = {},
        onFilterClick = {},
        onVacancyClick = {}
    )
}

@Preview(name = "Empty Result", showBackground = true)
@Composable
fun PreviewSearchScreenEmptyResult() {
    SearchScreen(
        state = SearchUiState.EmptyResult,
        query = "Senior Kotlin Architect",
        onQueryChange = {},
        onClearClick = {},
        onSearchClick = {},
        onFilterClick = {},
        onVacancyClick = {}
    )
}

@Preview(name = "Error", showBackground = true)
@Composable
fun PreviewSearchScreenError() {
    SearchScreen(
        state = SearchUiState.Error("Ошибка загрузки данных"),
        query = "QA Engineer",
        onQueryChange = {},
        onClearClick = {},
        onSearchClick = {},
        onFilterClick = {},
        onVacancyClick = {}
    )
}

@Preview(name = "Success", showBackground = true)
@Composable
fun PreviewSearchScreenSuccess() {
    val sampleItems = listOf(
        VacancyUi("1", "Android Developer", "Москва", "150 000 ₽", "VK", null),
        VacancyUi("2", "Kotlin Engineer", "Санкт-Петербург", "200 000 ₽", "Яндекс", null),
        VacancyUi("3", "QA Engineer", "Казань", null, "Сбер", null)
    )
    SearchScreen(
        state = SearchUiState.Success(sampleItems),
        query = "Developer",
        onQueryChange = {},
        onClearClick = {},
        onSearchClick = {},
        onFilterClick = {},
        onVacancyClick = {}
    )
}

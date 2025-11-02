package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.search.UiSpec.BODY_FONT_SIZE
import ru.practicum.android.diploma.ui.search.UiSpec.ICON_SIZE
import ru.practicum.android.diploma.ui.search.UiSpec.ICON_SIZE1
import ru.practicum.android.diploma.ui.search.UiSpec.PLACEHOLDER_VERTICAL_PADDING
import ru.practicum.android.diploma.ui.search.UiSpec.SCREEN_PADDING_H
import ru.practicum.android.diploma.ui.search.UiSpec.SEARCH_FIELD_HEIGHT
import ru.practicum.android.diploma.ui.search.UiSpec.SEARCH_FIELD_VERTICAL_PADDING
import ru.practicum.android.diploma.ui.search.UiSpec.SEARCH_LOADING_DELAY_MS
import ru.practicum.android.diploma.ui.search.UiSpec.TITLE_FONT_SIZE
import ru.practicum.android.diploma.ui.search.UiSpec.TOP_BAR_ACTION_END_PADDING
import ru.practicum.android.diploma.ui.search.UiSpec.TOP_BAR_ACTION_TOUCH
import ru.practicum.android.diploma.ui.search.UiSpec.TOP_BAR_HEIGHT

data class VacancyUi(
    val id: String,
    val title: String,
    val city: String,
    val salary: String?,
    val company: String?,
    val logoUrl: String? = null // нужно соостветсвенно внести измы в прочих оъектах
)

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Typing : SearchUiState
    data object NoInternet : SearchUiState
    data object EmptyResult : SearchUiState
    data class Success(val items: List<VacancyUi>) : SearchUiState
    data class Error(val message: String? = null) : SearchUiState
}

private object UiSpec {
    const val SEARCH_LOADING_DELAY_MS = 2000L
    val TOP_BAR_HEIGHT = 64.dp
    val TOP_BAR_ACTION_TOUCH = 40.dp
    val ICON_SIZE = 24.dp
    val SCREEN_PADDING_H = 16.dp
    val TOP_BAR_ACTION_END_PADDING = 12.dp
    val SEARCH_FIELD_HEIGHT = 56.dp
    val SEARCH_FIELD_VERTICAL_PADDING = 4.dp
    val PLACEHOLDER_VERTICAL_PADDING = 27.dp
    val TITLE_FONT_SIZE = 22.sp
    val BODY_FONT_SIZE = 16.sp
    val ICON_SIZE1 = 0.dp
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
    var textState by remember { mutableStateOf(query) }
    var currentState by remember { mutableStateOf<SearchUiState>(SearchUiState.Idle) }

    LaunchedEffect(textState) {
        if (textState.isNotEmpty()) {
            currentState = SearchUiState.Typing
            delay(SEARCH_LOADING_DELAY_MS)
            currentState = SearchUiState.Loading
        } else {
            currentState = SearchUiState.Idle
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(TOP_BAR_HEIGHT),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(R.string.search_job),
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                                fontSize = TITLE_FONT_SIZE,
                                color = colorResource(R.color.black_text)
                            )
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = SEARCH_FIELD_VERTICAL_PADDING),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier.size(TOP_BAR_ACTION_TOUCH)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = stringResource(R.string.filters_settings),
                                modifier = Modifier.size(ICON_SIZE),
                                tint = colorResource(R.color.black_text)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = colorResource(R.color.black_text),
                    actionIconContentColor = colorResource(R.color.black_text)
                )
            )
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

            when (currentState) {
                SearchUiState.Idle -> Placeholder(
                    image = R.drawable.search_placeholder_euy
                )

                SearchUiState.Typing -> {
                    Spacer(Modifier.height(ICON_SIZE1))
                }

                SearchUiState.Loading -> LoadingPlaceholder()
                else -> {}
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
            .padding(horizontal = SCREEN_PADDING_H, vertical = PLACEHOLDER_VERTICAL_PADDING)
            .height(SEARCH_FIELD_HEIGHT),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                fontSize = BODY_FONT_SIZE,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.color_gray_search_placeholder)
            )
        },
        textStyle = TextStyle(
            fontSize = BODY_FONT_SIZE,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_gray),
            unfocusedContainerColor = colorResource(R.color.light_gray),
            disabledContainerColor = colorResource(R.color.light_gray),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.Blue
        ),
        shape = RoundedCornerShape(TOP_BAR_ACTION_END_PADDING),
        leadingIcon = null,
        trailingIcon = {
            Row(
                modifier = Modifier.padding(end = SCREEN_PADDING_H),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClear, modifier = Modifier.size(ICON_SIZE)) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.clear),
                            tint = Color.Black
                        )
                    }
                } else {
                    IconButton(onClick = onSubmit, modifier = Modifier.size(ICON_SIZE)) {
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
internal fun VacancyList( // потом нужно будет вернуть приват!! не забыть
    items: List<VacancyUi>,
    onItemClick: (VacancyUi) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = SCREEN_PADDING_H, end = SCREEN_PADDING_H, bottom = SCREEN_PADDING_H)
    ) {
        items(items, key = { it.id }) { item ->
            VacancyListItem(item = item, onClick = { onItemClick(item) })
            Divider()
        }
    }
}

@Composable
private fun VacancyListItem(item: VacancyUi, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = TOP_BAR_ACTION_END_PADDING)
    ) {
        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
        item.company?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
        Row {
            Text(text = item.city, style = MaterialTheme.typography.bodyMedium)
            item.salary?.let {
                Text(
                    text = "  •  $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Placeholder(image: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SCREEN_PADDING_H),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
        }
    }
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
        VacancyUi("1", "Android Developer", "Москва", "150 000 ₽", "VK"),
        VacancyUi("2", "Kotlin Engineer", "Санкт-Петербург", "200 000 ₽", "Яндекс"),
        VacancyUi("3", "QA Engineer", "Казань", null, "Сбер")
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

package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R

data class VacancyUi(
    val id: String,
    val title: String,
    val city: String,
    val salary: String?,
    val company: String?
)

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
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
    var textState by remember { mutableStateOf(query) }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(64.dp),
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
                                fontSize = 22.sp,
                                color = colorResource(R.color.black_text)
                            )
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = stringResource(R.string.filters_settings),
                                modifier = Modifier.size(24.dp),
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

            when (state) {
                SearchUiState.Idle -> Placeholder(
                    image = R.drawable.search_placeholder,
                    text = stringResource(R.string.search_hint)
                )
                SearchUiState.Loading -> LoadingPlaceholder()
                SearchUiState.NoInternet -> Placeholder(
                    image = R.drawable.no_internet_placeholder,
                    text = stringResource(R.string.placeholder_no_internet)
                )
                SearchUiState.EmptyResult -> Placeholder(
                    image = R.drawable.no_vacanc_placeholder,
                    text = stringResource(R.string.placeholder_error)
                )
                is SearchUiState.Error -> Placeholder(
                    image = R.drawable.no_vacanc_placeholder,
                    text = state.message ?: stringResource(R.string.placeholder_error)
                )
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.color_gray_search_placeholder)
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
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
        shape = RoundedCornerShape(12.dp),
        leadingIcon = null,
        trailingIcon = {
            Row(
                modifier = Modifier.padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.clear),
                            tint = Color.Black
                        )
                    }
                } else {
                    IconButton(onClick = onSubmit, modifier = Modifier.size(24.dp)) {
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
private fun VacancyList(
    items: List<VacancyUi>,
    onItemClick: (VacancyUi) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
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
            .padding(vertical = 12.dp)
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
private fun Placeholder(image: Int, text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
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

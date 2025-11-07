package ru.practicum.android.diploma.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyUi
import ru.practicum.android.diploma.presentation.search.SearchState
import ru.practicum.android.diploma.ui.theme.AnimationDurations
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onVacancyClick: (VacancyUi) -> Unit,
    onLoadNextPage: () -> Unit = {},
    onDismissPaginationError: () -> Unit = {}
) {
    var textState by remember(query) { mutableStateOf(query) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    HandleFocusOnStateChange(
        state = state,
        keyboardController = keyboardController,
        focusManager = focusManager
    )

    Box(modifier = Modifier.fillMaxSize()) {
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
                    .padding(
                        top = inner.calculateTopPadding(),
                        start = inner.calculateStartPadding(LayoutDirection.Ltr),
                        end = inner.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
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
                    onSubmit = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onSearchClick()
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val initialCount = remember {
                        mutableIntStateOf(0)
                    }
                    LaunchedEffect(state) {
                        if (state is SearchState.Success) {
                            initialCount.intValue = state.found
                        }
                    }
                    val listState = rememberLazyListState()

                    SearchContent(
                        state = state,
                        onVacancyClick = onVacancyClick,
                        onLoadNextPage = onLoadNextPage,
                        listState = listState,
                        initialCount = initialCount.intValue
                    )
                }
            }
        }

        PaginatorErrorToast(
            state = state,
            onDismiss = onDismissPaginationError
        )
    }
}

@Composable
private fun HandleFocusOnStateChange(
    state: SearchState,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    LaunchedEffect(state) {
        when (state) {
            is SearchState.Success,
            is SearchState.EmptyResult,
            is SearchState.Loading,
            is SearchState.NoConnection,
            is SearchState.Error -> {
                keyboardController?.hide()
                focusManager.clearFocus()
            }

            else -> Unit
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
private fun SearchContent(
    state: SearchState,
    onVacancyClick: (VacancyUi) -> Unit,
    onLoadNextPage: () -> Unit,
    listState: LazyListState,
    initialCount: Int
) {
    when (state) {
        SearchState.Initial -> {
            Placeholder(imageRes = R.drawable.search_placeholder_euy, text = "")
        }

        SearchState.Loading -> LoadingPlaceholder()

        is SearchState.Success -> {
            VacancyList(
                items = state.vacancies,
                onItemClick = onVacancyClick,
                onLoadMore = onLoadNextPage,
                hasMorePages = state.hasMorePages,
                count = initialCount,
                listState = listState
            )
        }

        is SearchState.LoadingNextPage -> {
            VacancyList(
                items = state.currentVacancies,
                onItemClick = onVacancyClick,
                onLoadMore = onLoadNextPage,
                hasMorePages = true,
                isLoadingMore = true,
                count = initialCount,
                listState = listState
            )
        }

        is SearchState.PaginationError -> {
            VacancyList(
                items = state.currentVacancies,
                onItemClick = onVacancyClick,
                onLoadMore = onLoadNextPage,
                hasMorePages = state.currentPage < state.totalPages - 1,
                count = initialCount,
                listState = listState
            )
        }

        is SearchState.EmptyResult -> {
            Placeholder(
                imageRes = R.drawable.no_vacanc_placeholder,
                text = stringResource(R.string.placeholder_nothing_found)
            )
        }

        SearchState.NoConnection -> {
            Placeholder(
                imageRes = R.drawable.no_internet_placeholder,
                text = stringResource(R.string.placeholder_no_internet)
            )
        }

        is SearchState.Error -> {
            Placeholder(
                imageRes = R.drawable.server_not_responding_placeholder,
                text = stringResource(R.string.placeholder_error)
            )
        }
    }
}

@Composable
internal fun VacancyList(
    items: List<VacancyUi>,
    onItemClick: (VacancyUi) -> Unit,
    onLoadMore: () -> Unit = {},
    hasMorePages: Boolean = false,
    isLoadingMore: Boolean = false,
    count: Int,
    listState: LazyListState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.padding_35))
            }

            items(items, key = { it.id }) { item ->
                VacancyListItem(item = item, onClick = { onItemClick(item) })
            }

            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.padding_16),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Dimens.dp_32),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (hasMorePages && !isLoadingMore && items.isNotEmpty()) {
                item {
                    LaunchedEffect(Unit) {
                        onLoadMore()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.padding_1)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.padding_4)
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .width(Dimens.size_184)
                    .height(Dimens.size_27),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Dimens.corner),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.jobs_found,
                            count = count,
                            count
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

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
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.padding_16))

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun CustomToast(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(AnimationDurations.TOAST_DISPLAY_DURATION)
        visible = false
        delay(AnimationDurations.TOAST_ANIMATION_DURATION)
        onDismiss()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .width(Dimens.size_328)
                    .height(Dimens.dp_56)
                    .padding(bottom = Dimens.padding_16),
                shape = RoundedCornerShape(Dimens.corner),
                colors = CardDefaults.cardColors(
                    containerColor = Red
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = Dimens.padding_4,
                            bottom = Dimens.padding_4,
                            start = Dimens.padding_8,
                            end = Dimens.padding_8
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun PaginatorErrorToast(
    state: SearchState,
    onDismiss: () -> Unit
) {
    if (state is SearchState.PaginationError) {
        val message = when (state.errorType) {
            SearchState.PaginationError.ErrorType.NO_CONNECTION ->
                stringResource(R.string.toast_check_internet)

            SearchState.PaginationError.ErrorType.SERVER_ERROR ->
                stringResource(R.string.toast_error_occurred)
        }

        CustomToast(
            message = message,
            onDismiss = onDismiss
        )
    }
}

@Preview(name = "Idle", showBackground = true)
@Composable
fun PreviewSearchScreenIdle() {
    AppTheme {
        SearchScreen(
            state = SearchState.Initial,
            query = "",
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onFilterClick = {},
            onVacancyClick = {},
            onDismissPaginationError = {}
        )
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
fun PreviewSearchScreenLoading() {
    AppTheme {
        SearchScreen(
            state = SearchState.Loading,
            query = "Android Developer",
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onFilterClick = {},
            onVacancyClick = {},
            onDismissPaginationError = {}
        )
    }
}

@Preview(name = "No Internet", showBackground = true)
@Composable
fun PreviewSearchScreenNoInternet() {
    AppTheme {
        SearchScreen(
            state = SearchState.NoConnection,
            query = "Designer",
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onFilterClick = {},
            onVacancyClick = {},
            onDismissPaginationError = {}
        )
    }
}

@Preview(name = "Empty Result", showBackground = true)
@Composable
fun PreviewSearchScreenEmptyResult() {
    AppTheme {
        SearchScreen(
            state = SearchState.EmptyResult(query = "Senior Kotlin Architect"),
            query = "Senior Kotlin Architect",
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onFilterClick = {},
            onVacancyClick = {},
            onDismissPaginationError = {}
        )
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
fun PreviewSearchScreenError() {
    AppTheme {
        SearchScreen(
            state = SearchState.Error(""),
            query = "QA Engineer",
            onQueryChange = {},
            onClearClick = {},
            onSearchClick = {},
            onFilterClick = {},
            onVacancyClick = {},
            onDismissPaginationError = {}
        )
    }
}

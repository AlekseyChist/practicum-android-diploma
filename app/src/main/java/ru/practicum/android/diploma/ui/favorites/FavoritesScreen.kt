package ru.practicum.android.diploma.ui.favorites

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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.favorites.FavoritesState
import ru.practicum.android.diploma.ui.favorites.mock.FavoritesStateProvider
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun FavoritesScreen(
    state: FavoritesState
) {
    Scaffold(
        topBar = { TopBar() },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = remember { WindowInsets(0, 0, 0, 0) }
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
            when (state) {
                is FavoritesState.Content -> {
                    ContentSection()
                }

                is FavoritesState.Empty -> {
                    ErrorSection(
                        idRes = R.drawable.favorites_is_empty,
                        message = stringResource(R.string.list_is_empty)
                    )
                }

                is FavoritesState.Error -> {
                    ErrorSection(
                        idRes = R.drawable.no_vacanc_placeholder,
                        message = stringResource(R.string.placeholder_nothing_found)
                    )
                }

                is FavoritesState.Initial, FavoritesState.Loading -> {
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
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .heightIn(Dimens.appBarHeight)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
            title = {
                Text(
                    text = stringResource(R.string.favorites),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            windowInsets = WindowInsets.statusBars
        )
    }
}

@Composable
private fun ContentSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.padding_16)
    ) {
        // list
    }
}

@Composable
private fun ErrorSection(
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
                contentDescription = "Ошибка загрузки",
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
fun FavoritesScreenPreview(
    @PreviewParameter(FavoritesStateProvider::class) state: FavoritesState
) {
    AppTheme {
        FavoritesScreen(state = state)
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FavoritesScreenDarkPreview(
    @PreviewParameter(FavoritesStateProvider::class) state: FavoritesState
) {
    AppTheme {
        FavoritesScreen(state = state)
    }
}

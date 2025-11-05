package ru.practicum.android.diploma.ui.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun TeamScreen() {
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
            Column(
                modifier = Modifier
                    .padding(Dimens.padding_16)
            ) {
                Text(
                    text = stringResource(R.string.list_of_developers),
                    modifier = Modifier.padding(bottom = Dimens.padding_24),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                NameField(R.string.AC)
                NameField(R.string.ET)
                NameField(R.string.IV)
                NameField(R.string.KK)
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
                    text = stringResource(R.string.team),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            windowInsets = WindowInsets.statusBars
        )
    }
}

@Composable
private fun NameField(
    nameId: Int
) {
    Text(
        text = stringResource(nameId),
        modifier = Modifier.padding(
            bottom = Dimens.padding_16
        ),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Preview(showBackground = true)
@Composable
fun TeamScreenPreview() {
    AppTheme {
        TeamScreen()
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TeamScreenDarkPreview() {
    AppTheme {
        TeamScreen()
    }
}

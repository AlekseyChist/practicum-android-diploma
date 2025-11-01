package ru.practicum.android.diploma.ui.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun TeamScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = stringResource(R.string.team),
            modifier = Modifier.padding(
                start = Dimens.dp_24,
                top = Dimens.dp_56,
                bottom = Dimens.dp_8
            ),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.list_of_developers),
            modifier = Modifier.padding(
                start = Dimens.dp_24,
                top = Dimens.dp_32,
                bottom = Dimens.dp_8
            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.AC),
            modifier = Modifier.padding(
                start = Dimens.dp_24,
                top = Dimens.dp_32,
                bottom = Dimens.dp_8
            ),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.ET),
            modifier = Modifier.padding(
                start = Dimens.dp_24,
                top = Dimens.dp_8,
                bottom = Dimens.dp_8
            ),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.IV),
            modifier = Modifier.padding(
                start = Dimens.dp_24,
                top = Dimens.dp_8,
                bottom = Dimens.dp_8
            ),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.KK),
            modifier = Modifier.padding(
                start = Dimens.dp_24,
                top = Dimens.dp_8,
                bottom = Dimens.dp_8
            ),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
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

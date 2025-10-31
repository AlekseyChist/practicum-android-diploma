package ru.practicum.android.diploma.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

object TeamScreenDimens {
    val dp_8 = 8.dp
    val dp_16 = 16.dp
    val dp_24 = 24.dp
    val dp_32 = 32.dp
    val dp_56 = 56.dp
    val sp_16 = 16.sp
    val sp_22 = 22.sp
    val sp_32 = 32.sp
}

@Composable
fun TeamScreen() {
    Column {
        Text(
            text = stringResource(R.string.team),
            modifier = Modifier.padding(start = TeamScreenDimens.dp_24, top = TeamScreenDimens.dp_56, bottom = TeamScreenDimens.dp_8),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = TeamScreenDimens.sp_22,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.list_of_developers),
            modifier = Modifier.padding(start = TeamScreenDimens.dp_24, top = TeamScreenDimens.dp_32, bottom = TeamScreenDimens.dp_8),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = TeamScreenDimens.sp_32,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.AC),
            modifier = Modifier.padding(start = TeamScreenDimens.dp_24, top = TeamScreenDimens.dp_32, bottom = TeamScreenDimens.dp_8),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = TeamScreenDimens.sp_16,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.ET),
            modifier = Modifier.padding(start = TeamScreenDimens.dp_24, top = TeamScreenDimens.dp_8, bottom = TeamScreenDimens.dp_8),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = TeamScreenDimens.sp_16,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.IV),
            modifier = Modifier.padding(start = TeamScreenDimens.dp_24, top = TeamScreenDimens.dp_8, bottom = TeamScreenDimens.dp_8),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = TeamScreenDimens.sp_16,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.KK),
            modifier = Modifier.padding(start = TeamScreenDimens.dp_24, top = TeamScreenDimens.dp_8, bottom = TeamScreenDimens.dp_8),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = TeamScreenDimens.sp_16,
                fontWeight = FontWeight.Medium
            ),
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

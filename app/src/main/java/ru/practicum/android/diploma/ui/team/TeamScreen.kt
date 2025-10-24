package ru.practicum.android.diploma.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R

@Composable
fun TeamScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.Team)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TeamScreenPreview() {
    MaterialTheme {
        Surface {
            TeamScreen()
        }
    }
}

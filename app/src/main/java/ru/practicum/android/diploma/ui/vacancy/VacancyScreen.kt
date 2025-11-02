package ru.practicum.android.diploma.ui.vacancy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import android.content.Intent
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.formatForDisplay
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailState
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.vacancy.mock.VacancyStateProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyScreen(
    state: VacancyDetailState,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .heightIn(64.dp)
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
                            text = stringResource(R.string.vacancy),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    actions = {
                        Row(modifier = Modifier.padding(0.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.share_icon),
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .clickable { onShareClick() }
                            )
                            Icon(
                                painter = painterResource(R.drawable.favorite_icon),
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .clickable { onFavoriteClick() }
                            )
                        }
                    },
                    windowInsets = WindowInsets.statusBars
                )
            }
        },
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
                is VacancyDetailState.Initial -> {
                    // заглушка
                }

                is VacancyDetailState.Loading -> {
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

                is VacancyDetailState.NoConnection -> {
                    ErrorSection(
                        idRes = R.drawable.no_internet_placeholder,
                        message = "Нет интернета"
                    )
                }

                is VacancyDetailState.Success -> {
                    VacancyDetailView(
                        vacancy = state.vacancy
                    )
                }

                is VacancyDetailState.NotFound -> {
                    ErrorSection(
                        idRes = R.drawable.vacancy_not_found_error,
                        message = "Вакансия не найдена или удалена"
                    )
                }

                is VacancyDetailState.ServerError -> {
                   ErrorSection(
                       idRes = R.drawable.vacancy_server_error,
                       message = "Ошибка сервера"
                   )
                }
            }
        }
    }
}

@Composable
private fun VacancyCard(
    vacancy: Vacancy
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(vacancy.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "vacancy image",
                    placeholder = painterResource(R.drawable.placeholder_32px),
                    error = painterResource(R.drawable.placeholder_32px),
                    fallback = painterResource(R.drawable.placeholder_32px),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column() {
                Text(
                    text = vacancy.employer.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                )
                Text(
                    text = vacancy.area.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
fun VacancyDetailView(
    vacancy: Vacancy
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column() {
            Text(
                text = vacancy.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            vacancy.salary?.formatForDisplay()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        VacancyCard(vacancy)
    }
    ScrollableDetails(vacancy)
}

@Composable
fun ScrollableDetails(
    vacancy: Vacancy
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.experience),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            vacancy.experience?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(bottom = 32.dp)
        ) {
            val employmentName = vacancy.employment?.name
            val scheduleName = vacancy.schedule?.name

            val info = listOfNotNull(employmentName, scheduleName).joinToString(", ")
            Text(
                text = info,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.vacancy_description),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            Text(
                text = vacancy.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        if (!vacancy.keySkills.isEmpty()) {
            Column() {
                Text(
                    text = stringResource(R.string.key_skills),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                LabeledListSection(
                    items = vacancy.keySkills
                )
            }
        }

        val context = LocalContext.current
        vacancy.contacts?.let { contacts ->
            if (
                !contacts.email.isNullOrBlank() ||
                contacts.phones.isNotEmpty()
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.contacts),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    contacts.name?.takeIf { it.isNotBlank() }?.let { name ->
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    contacts.email?.takeIf { it.isNotBlank() }?.let { email ->
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = "mailto:$email".toUri()
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        context.getString(R.string.email_app)
                                    )
                                )
                            }
                        )
                    }
                    contacts.phones.forEach { phone ->
                        Text(
                            text = phone,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = "tel:$phone".toUri()
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        context.getString(R.string.call_app)
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LabeledListSection(
    items: List<String>
) {
    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
            }
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
            .padding(16.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

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
fun VacancyScreenPreview(
    @PreviewParameter(VacancyStateProvider::class) state: VacancyDetailState
) {
    AppTheme {
        VacancyScreen(
            state = state,
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun VacancyScreenDarkPreview(
    @PreviewParameter(VacancyStateProvider::class) state: VacancyDetailState
) {
    AppTheme {
        VacancyScreen(
            state = state,
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {}
        )
    }
}

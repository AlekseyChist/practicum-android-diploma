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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.formatForDisplay
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailState
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.ui.vacancy.mock.VacancyStateProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyScreen(
    state: VacancyDetailState,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onFavoriteClick: () -> Unit,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                state = state,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick
            )
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
                is VacancyDetailState.Loading, VacancyDetailState.Initial -> {
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

                is VacancyDetailState.Success -> {
                    VacancyDetailView(
                        vacancy = state.vacancy,
                        onEmailClick = onEmailClick,
                        onPhoneClick = onPhoneClick
                    )
                }

                is VacancyDetailState.NotFound -> {
                    ErrorSection(
                        idRes = R.drawable.vacancy_not_found_error,
                        message = stringResource(R.string.vacancy_not_found)
                    )
                }

                is VacancyDetailState.ServerError -> {
                    ErrorSection(
                        idRes = R.drawable.vacancy_server_error,
                        message = stringResource(R.string.server_error)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    state: VacancyDetailState,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .heightIn(Dimens.appBarHeight)
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
                (state as? VacancyDetailState.Success)?.let { successState ->
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.share_icon),
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable { onShareClick(successState.vacancy.url) }
                        )

                        Icon(
                            painter = painterResource(
                                if (successState.isFavorite) {
                                    R.drawable.favorite_icon_filled
                                } else {
                                    R.drawable.favorite_icon
                                }
                            ),
                            contentDescription = "Favorite",
                            tint = if (successState.isFavorite) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            modifier = Modifier
                                .clickable { onFavoriteClick() }
                        )
                    }
                }
            },
            windowInsets = WindowInsets.statusBars
        )
    }
}

@Composable
private fun VacancyCard(
    vacancy: Vacancy
) {
    Card(
        shape = RoundedCornerShape(Dimens.corner),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.padding_16),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(vacancy.employer.logoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "company logo",
                placeholder = painterResource(R.drawable.placeholder2),
                error = painterResource(R.drawable.placeholder2),
                modifier = Modifier
                    .size(Dimens.size_48)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(Dimens.corner)
                    )
                    .padding(end = 0.dp)
                    .clip(RoundedCornerShape(Dimens.corner)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.size(Dimens.padding_8))

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
    vacancy: Vacancy,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.padding_8, horizontal = Dimens.padding_16)
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
            .padding(
                top = Dimens.padding_16,
                start = Dimens.padding_16,
                end = Dimens.padding_16,
                bottom = Dimens.padding_24
            )
    ) {
        VacancyCard(vacancy)
    }
    ScrollableDetails(
        vacancy,
        onEmailClick,
        onPhoneClick
    )
}

@Composable
fun ScrollableDetails(
    vacancy: Vacancy,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.padding_16)
    ) {
        Column(modifier = Modifier.padding(bottom = Dimens.padding_8)) {
            Text(
                text = stringResource(R.string.experience),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            vacancy.experience?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        EmploymentScheduleSection(vacancy.employment?.name, vacancy.schedule?.name)
        DescriptionSection(vacancy.description)
        KeySkillsSection(vacancy.keySkills)
        ContactsSection(
            contacts = vacancy.contacts,
            onEmailClick = onEmailClick,
            onPhoneClick = onPhoneClick
        )
    }
}

@Composable
private fun EmploymentScheduleSection(employmentName: String?, scheduleName: String?) {
    Row(modifier = Modifier.padding(bottom = Dimens.padding_32)) {
        val info = listOfNotNull(employmentName, scheduleName).joinToString(", ")
        Text(
            text = info,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column(modifier = Modifier.padding(bottom = Dimens.padding_24)) {
        Text(
            text = stringResource(R.string.vacancy_description),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = Dimens.padding_16)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun KeySkillsSection(keySkills: List<String>) {
    if (keySkills.isNotEmpty()) {
        Column {
            Text(
                text = stringResource(R.string.key_skills),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = Dimens.padding_16)
            )
            LabeledListSection(items = keySkills)
        }
    }
}

@Composable
private fun ContactsSection(
    contacts: Contacts?,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String) -> Unit
) {
    contacts?.let {
        if (!it.email.isNullOrBlank() || it.phones.isNotEmpty()) {
            Column(modifier = Modifier.padding(bottom = Dimens.padding_24)) {
                Text(
                    text = stringResource(R.string.contacts),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = Dimens.padding_16)
                )
                it.name?.takeIf(String::isNotBlank)?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                it.email?.takeIf(String::isNotBlank)?.let { email ->
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onEmailClick(email) }
                    )
                }
                it.phones.forEach { phone ->
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onPhoneClick(phone) }
                    )
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
        modifier = Modifier.padding(bottom = Dimens.padding_16)
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = Dimens.padding_4)
            ) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(end = Dimens.padding_8)
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
fun VacancyScreenPreview(
    @PreviewParameter(VacancyStateProvider::class) state: VacancyDetailState
) {
    AppTheme {
        VacancyScreen(
            state = state,
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {},
            onEmailClick = {},
            onPhoneClick = {}
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
            onFavoriteClick = {},
            onEmailClick = {},
            onPhoneClick = {}
        )
    }
}

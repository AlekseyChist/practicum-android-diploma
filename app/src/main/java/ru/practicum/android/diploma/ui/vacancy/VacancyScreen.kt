package ru.practicum.android.diploma.ui.vacancy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.formatForDisplay
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailState
import ru.practicum.android.diploma.ui.theme.AppTheme

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
//                    LoadingView()
                }

                is VacancyDetailState.Error -> {
//                    ErrorView(
//                        message = state.message,
//                        onRetryClick = onShareClick
//                    )
                }

                is VacancyDetailState.NoConnection -> {
//                    NoConnectionView(onRetryClick = onShareClick)
                }

                is VacancyDetailState.Success -> {
                    VacancyDetailView(
                        vacancy = state.vacancy
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
    }
}

@Preview(showBackground = true)
@Composable
fun VacancyScreenPreview() {
    val mockVacancy = Vacancy(
        id = "1",
        name = "Android-разработчик",
        employer = Employer(
            id = "123",
            name = "Еда",
            logoUrl = null
        ),
        area = Area(
            id = "213",
            name = "Москва"
        ),
        salary = Salary(
            from = 100000,
            to = 200000,
            currency = "RUR"
        ),
        experience = Experience(
            id = "exp_1",
            name = "От 1 года до 4 лет"
        ),
        employment = Employment(
            id = "emp_1",
            name = "Полная занятость"
        ),
        schedule = Schedule(
            id = "sched_1",
            name = "Удалённая работа"
        ),
        description = "Разработка и поддержка Android-приложений компании.",
        keySkills = listOf("Kotlin", "Jetpack Compose", "MVVM"),
        contacts = Contacts(
            name = "HR Анна",
            email = "hr@eda.ru",
            phones = listOf("+7 (999) 123-45-67")
        ),
        address = "Москва, ул. Ленина, 10",
        url = "https://png.pngtree.com/thumb_back/fh260/background/20230610/pngtree-picture-of-a-blue-bird-on-a-black-background-image_2937385.jpg",
        isFavorite = false
    )

    AppTheme {
        VacancyScreen(
            state = VacancyDetailState.Success(
                vacancy = mockVacancy,
                isFavorite = false
            ),
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
fun VacancyScreenDarkPreview() {
    val mockVacancy = Vacancy(
        id = "1",
        name = "Android-разработчик",
        employer = Employer(
            id = "123",
            name = "Еда",
            logoUrl = null
        ),
        area = Area(
            id = "213",
            name = "Белгород"
        ),
        salary = Salary(
            from = 100000,
            to = 200000,
            currency = "RUR"
        ),
        experience = Experience(
            id = "exp_1",
            name = "От 1 года до 3 лет"
        ),
        employment = Employment(
            id = "emp_1",
            name = "Полная занятость"
        ),
        schedule = Schedule(
            id = "sched_1",
            name = "Удалённая работа"
        ),
        description = "Разработка и поддержка Android-приложений компании.",
        keySkills = listOf("Kotlin", "Jetpack Compose", "MVVM"),
        contacts = Contacts(
            name = "HR Анна",
            email = "hr@eda.ru",
            phones = listOf("+7 (999) 123-45-67")
        ),
        address = "Москва, ул. Ленина, 10",
        url = "https://company.ru/vacancy/1",
        isFavorite = false
    )

    AppTheme {
        VacancyScreen(
            state = VacancyDetailState.Success(
                vacancy = mockVacancy,
                isFavorite = true
            ),
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {}
        )
    }
}

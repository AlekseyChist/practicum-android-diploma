package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen()
            }
        }
    }
}

@Composable
fun DebouncedSearchField(
    delayMs: Long = 500L,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    // Само текстовое поле
    TextField(
        value = query,
        onValueChange = { query = it },
        label = { Text("Введите запрос") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

    // Debounce через Flow
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(delayMs)
            .distinctUntilChanged()
            .collectLatest { text ->
                onSearch(text)
            }
    }
}

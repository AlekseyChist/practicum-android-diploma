package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    action: (T) -> Unit
): DebouncedAction<T> = DebouncedAction(delayMillis, coroutineScope, action)

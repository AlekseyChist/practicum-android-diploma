package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebouncedAction<T>(
    private val delayMillis: Long,
    private val coroutineScope: CoroutineScope,
    private val action: (T) -> Unit
) {
    private var job: Job? = null

    operator fun invoke(param: T) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(delayMillis)
            action(param)
        }
    }

    fun cancelPending() {
        job?.cancel()
    }
}

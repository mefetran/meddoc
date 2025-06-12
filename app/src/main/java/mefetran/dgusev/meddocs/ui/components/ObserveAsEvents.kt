package mefetran.dgusev.meddocs.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Subscribes to a [flow] of events and invokes [onEvent] for each emitted item,
 * only while the current [Lifecycle] is at least in the [Lifecycle.State.STARTED] state.
 *
 * This composable is intended for handling one-time events such as navigation,
 * showing snackbars, dialogs, etc. It leverages the current [LocalLifecycleOwner]
 * to automatically respect the component's lifecycle (e.g., Activity or Fragment).
 *
 * @param flow The flow of events to observe.
 * @param key1 An optional key to restart the effect when it changes.
 * @param key2 An optional second key to restart the effect when it changes.
 * @param onEvent Callback triggered for each event emitted by the [flow].
 *
 * Example usage:
 * ```
 * ObserveAsEvents(viewModel.events) { event ->
 *     when (event) {
 *         is UiEvent.ShowSnackbar -> showSnackbar(event.message)
 *         is UiEvent.Navigate -> navController.navigate(event.route)
 *     }
 * }
 * ```
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (event: T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle, flow, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
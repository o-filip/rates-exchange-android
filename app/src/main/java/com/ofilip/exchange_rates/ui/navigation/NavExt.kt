package com.ofilip.exchange_rates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

private const val NAV_RESULT_CALLBACK_KEY = "NavResultCallbackKey"

/**
 * Navigate to a destination and set a callback to receive a result when the destination pops.
 */
fun <T> NavController.navigateForResult(
    route: Any,
    navResultCallback: (T) -> Unit,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    currentBackStackEntry?.savedStateHandle?.set(NAV_RESULT_CALLBACK_KEY, navResultCallback)
    navigate(route, navOptions, navigatorExtras)
}

/**
 * Pop back to the previous destination and set a result to be received by the callback.
 */
fun <T> NavController.popBackStackWithResult(result: T) {
    previousBackStackEntry?.savedStateHandle
        ?.remove<((T) -> Unit)?>(NAV_RESULT_CALLBACK_KEY)
        ?.invoke(result)
    popBackStack()
}

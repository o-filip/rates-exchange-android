package com.ofilip.exchange_rates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun List<String>.encodeToNavPath(): String =
    joinToString(separator = ",") { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) }

fun String.decodeListStringFromNavPath(): List<String> =
    split(",").map { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }


fun <T> NavController.navigateForResult(
    route: String,
    navResultCallback: (T) -> Unit,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    setNavResultCallback(navResultCallback)
    navigate(route, navOptions, navigatorExtras)
}

private const val NavResultCallbackKey = "NavResultCallbackKey"

fun <T> NavController.setNavResultCallback(callback: (T) -> Unit) {
    currentBackStackEntry?.savedStateHandle?.set(NavResultCallbackKey, callback)
}

fun <T> NavController.popBackStackWithResult(result: T) {
    getNavResultCallback<T>()?.invoke(result)
    popBackStack()
}

fun <T> NavController.getNavResultCallback(): ((T) -> Unit)? {
    return previousBackStackEntry?.savedStateHandle?.remove(NavResultCallbackKey)
}
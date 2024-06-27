package com.ofilip.exchange_rates.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 *  Add the [Composable] to the [NavGraphBuilder]
 *
 * @param T type of destination
 * @param dest destination to be added
 * @param content composable for the destination
 */
fun <T : Dest> NavGraphBuilder.composable(
    dest: T,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = dest.route,
        arguments = dest.arguments,
        deepLinks = dest.deepLinks,
        content = content
    )
}

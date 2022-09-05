package com.ofilip.exchange_rates.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

/**
 * Destination configuration used for navigation in compose nav controller
 */
interface Dest {
    val route: String
    val arguments: List<NamedNavArgument>
    val deepLinks: List<NavDeepLink>
}

/**
 * Default destination implementation
 */
class DefaultDest(override val route: String) : Dest {
    override val arguments: List<NamedNavArgument> = emptyList()
    override val deepLinks: List<NavDeepLink> = emptyList()
}

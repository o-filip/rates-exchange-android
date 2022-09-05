package com.ofilip.exchange_rates.ui.screen.home.component.themeToggle

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme

@Composable
fun ThemeToggleSection(
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val darkTheme = themeViewModel.darkTheme.collectAsState(initial = null).value ?: isSystemInDarkTheme()

    ThemeToggleSectionContent(
        modifier = modifier,
        darkTheme = darkTheme,
        onToggle = themeViewModel::setDarkTheme
    )
}

@Composable
fun ThemeToggleSectionContent(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { onToggle(!darkTheme) }
        ) {
            Icon(
                modifier = Modifier.size(45.dp),
                painter = painterResource(id = R.drawable.ic_theme),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeToggleSectionPreviewLight() {
    ExchangeRatesTheme {
        ThemeToggleSectionContent(
            darkTheme = false,
            onToggle = {}
        )
    }
}
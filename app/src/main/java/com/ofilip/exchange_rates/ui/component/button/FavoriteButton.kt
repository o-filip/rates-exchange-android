package com.ofilip.exchange_rates.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.R

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    iconTint: Color
) {
    IconButton(
        modifier = modifier,
        onClick = onFavoriteToggle,
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(
                id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outlined
            ),
            contentDescription = null,
            colorFilter = iconTint.let { ColorFilter.tint(it) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteButtonPreview() {
    FavoriteButton(
        isFavorite = true,
        onFavoriteToggle = {},
        iconTint = Color.Black
    )
}

package com.ofilip.exchange_rates.ui.component.field

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomInputChip(
    text: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    InputChip(
        modifier = Modifier.padding(4.dp),
        onClick = onOptionSelected,
        label = {
            Text(
                text,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.button
            )
        },
        selected = isSelected,
        shape = MaterialTheme.shapes.small,
        colors = InputChipDefaults.inputChipColors().copy(
            selectedContainerColor = MaterialTheme.colors.primary,
        ),
    )
}

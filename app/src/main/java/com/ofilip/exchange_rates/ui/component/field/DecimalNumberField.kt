package com.ofilip.exchange_rates.ui.component.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.ofilip.exchange_rates.ui.theme.Transparent
import java.text.DecimalFormat

class DecimalTextFieldValue constructor(
    val text: TextFieldValue = TextFieldValue(),
    val number: Double? = null
) {

    companion object {
        fun create(): DecimalTextFieldValue = DecimalTextFieldValue()

        fun createOrNull(text: TextFieldValue): DecimalTextFieldValue? {
            return if (text.text.isBlank()) {
                DecimalTextFieldValue(
                    text = text,
                    number = null
                )
            } else if (text.text.toDoubleOrNull() != null) {
                DecimalTextFieldValue(
                    text = text,
                    number = text.text.toDoubleOrNull()
                )
            } else {
                null
            }
        }

        fun create(number: Double?, maxDecimalPlaces: Int? = 2): DecimalTextFieldValue {
            val text = if (number == null) {
                TextFieldValue()
            } else {
                TextFieldValue(
                    DecimalFormat().also {
                        if (maxDecimalPlaces != null) it.maximumFractionDigits = maxDecimalPlaces
                        it.groupingSize = 0
                    }.format(number)
                )
            }

            return DecimalTextFieldValue(
                text = text,
                number = number
            )
        }
    }
}

@Composable
fun DecimalNumberField(
    modifier: Modifier = Modifier,
    value: DecimalTextFieldValue,
    onValueChanged: (DecimalTextFieldValue) -> Unit,
    leading: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        TextField(
            value = value.text,
            onValueChange = {
                DecimalTextFieldValue.createOrNull(it)?.let(onValueChanged)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Transparent,
                focusedLabelColor = MaterialTheme.colors.onSurface
            ),
            label = label,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = leading,
            isError = error != null
        )
        error?.invoke()
    }
}

@Preview(showBackground = true)
@Composable
fun DecimalNumberFieldPreview() {
    DecimalNumberField(
        value = DecimalTextFieldValue.create(99.5),
        onValueChanged = {}
    )
}

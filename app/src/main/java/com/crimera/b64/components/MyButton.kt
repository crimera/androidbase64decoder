package com.crimera.b64.components

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MyButton(text: String, onclick: () -> Unit) {
    FilledTonalButton(
        onClick = onclick,
    ) {
        Text(
            text = text
        )
    }
}

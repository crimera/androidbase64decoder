package com.crimera.b64.components

import android.content.Context
import android.content.Intent
import android.os.Process
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.crimera.b64.ui.Home
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(intent: Intent, context: Context) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        sheetShape = ShapeDefaults.Large,
        sheetContent = {
            if (!sheetState.isVisible) {
                Process.killProcess(Process.myPid())
                exitProcess(0)
            }

            Home(intent, context)
        },
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        content = {},
    )
}

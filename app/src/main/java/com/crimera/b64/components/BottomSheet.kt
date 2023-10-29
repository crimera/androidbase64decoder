package com.crimera.b64.components

import android.content.Context
import android.content.Intent
import android.os.Process
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.crimera.b64.ui.Home
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(intent: Intent, context: Context) {
    ModalBottomSheet(onDismissRequest = {
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }) {
        Home(intent, context)
    }
}
